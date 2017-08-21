package mydebts.android.app.feature.participant

import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.R
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.res.Resources
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class ParticipantPresenter @Inject constructor(
        private val screen: ParticipantScreen,
        private val participant: Participant?,
        private val personsSource: PersonsSource,
        private val rxUtil: RxUtil,
        private val res: Resources)
{

    private var person = Person()
    private var debt = .0
    private var debtString: CharSequence? = null

    private val persons = ArrayList<Person>()
    private val disposables = CompositeDisposable()

    fun onCreate() {
        participant?.person?.also {
            it.name?.let {
                screen.showName(it)
                screen.setNameEnabled(false)
                screen.requestFocusOnDebtInput()
            }
            person = it
        }

        participant?.debt?.let {
            debt = it
            screen.showDebt(it)
        }

        disposables.add(personsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .doOnSuccess { persons.addAll(it) }
                .map { it.map { it.name ?: "" } }
                .subscribe { persons -> screen.setPersonsSuggestions(persons) })
    }

    fun onDestroy() {
        disposables.clear()
    }

    fun onSuggestionItemClick(position: Int) {
        person = persons[position]
    }

    fun onNameChanged(name: CharSequence?) {
        person = Person(name = name.toString())
        screen.showNameError(name.takeIf { it.isNullOrEmpty() }?.let { res.string(R.string.error_no_name) })
    }

    fun onDebtChanged(debt: CharSequence?) {
        debtString = debt

        try {
            this.debt = debt.toString().toDouble()
            screen.showDebtError(null)
        } catch (e: NumberFormatException) {
            this.debt = .0
            screen.showDebtError(res.string(R.string.error_no_amount))
        }
    }

    fun onDoneClick() {
        if (person.name.isNullOrEmpty()) {
            screen.showNameError(res.string(R.string.error_no_name))
            return
        }

        if (debtString.isNullOrEmpty()) {
            screen.showDebtError(res.string(R.string.error_no_amount))
            return
        }

        val result = participant?.also { it.debt = debt } ?: Participant(person = person, debt = debt)
        screen.setResult(result)
        screen.finish()
    }
}
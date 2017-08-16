package mydebts.android.app.feature.participant

import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class ParticipantViewModel @Inject constructor(
        private val screen: ParticipantScreen,
        private val participant: Participant?,
        private val personsSource: PersonsSource,
        private val rxUtil: RxUtil)
{

    private var person = Person()
    private var debt = .0

    private val persons = ArrayList<Person>()
    private val disposables = CompositeDisposable()

    fun onCreate() {
        participant?.person?.also {
            it.name?.let {
                screen.showName(it)
                screen.setNameEnabled(false)
            }
            person = it
        }
        participant?.debt?.let { debt = it }
        screen.showDebt(debt)

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
    }

    fun onDebtChanged(debt: CharSequence?) {
        this.debt = debt.toString().toDoubleOrNull() ?: .0
    }

    fun onDoneClick() {
        val result = participant?.also { it.debt = debt } ?: Participant(person = person, debt = debt)
        screen.setResult(result)
        screen.finish()
    }
}
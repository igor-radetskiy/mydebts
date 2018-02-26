package mydebts.android.app.feature.participant

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.feature.event.BackNavigation
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class ParticipantViewModel(
        private val participant: Participant?,
        private val personsSource: PersonsSource,
        private val rxUtil: RxUtil,
        @ParticipantUi private val participantUiSubject: PublishSubject<Participant>) : ViewModel()
{
    private var person = participant?.person ?: Person()
    private var debtAmount = participant?.debt ?: .0

    private val personSuggestions = ArrayList<Person>()
    private val disposables = CompositeDisposable()

    private val _name = MutableLiveData<CharSequence>()
    internal val name: LiveData<CharSequence>
        get() {
            person.name?.let { _name.value = it }
            return _name
        }

    private val _nameEditEnabled = MutableLiveData<Boolean>()
    internal val nameEditEnabled: LiveData<Boolean>
        get() {
            _nameEditEnabled.value = participant != null
            return _nameEditEnabled
        }

    private val _isNameValid = MutableLiveData<Boolean>()
    internal val isNameValid: LiveData<Boolean>
        get() = _isNameValid

    private val _nameSuggestions = MutableLiveData<List<String>>()
    internal val nameSuggestions: LiveData<List<String>>
        get() {
            if (participant == null) {
                loadPersonSuggestions()
            }
            return _nameSuggestions
        }

    private val _debt = MutableLiveData<Double>()
    internal val debt: LiveData<Double>
        get() {
            participant?.run { _debt.value = debtAmount }
            return _debt
        }

    private val _isDebtValid = MutableLiveData<Boolean>()
    internal val isDebtValid: LiveData<Boolean>
        get() = _isDebtValid

    private val _backNavigation = MutableLiveData<BackNavigation>()
    internal val backNavigation: LiveData<BackNavigation>
        get() = _backNavigation

    override fun onCleared() {
        disposables.clear()
    }

    internal fun onNameChanged(text: CharSequence) {
        if (text.isEmpty()) {
            _isNameValid.value = false
        } else {
            person = Person(name = text.toString())
            _isNameValid.value = true
        }
    }

    internal fun onDebtChanged(text: CharSequence) {
        try {
            debtAmount = text.toString().toDouble()
            _isDebtValid.value = true
        } catch (e: NumberFormatException) {
            _isDebtValid.value = false
        }
    }

    internal fun onSuggestionItemClick(position: Int) {
        person = personSuggestions[position]
        _name.value = person.name
    }

    internal fun onDoneClick() {
        if (_isNameValid.value == null) {
            _isNameValid.value = false
            return
        }

        if (_isDebtValid.value == null) {
            _isDebtValid.value = false
            return
        }

        _isNameValid.value?.takeIf { it }
                ?.let { _isDebtValid.value }?.takeIf { it }
                ?.run {
                    val result = participant?.also { it.debt = debtAmount }
                            ?: Participant(person = person, debt = debtAmount)
                    participantUiSubject.onNext(result)

                    _backNavigation.value = BackNavigation()
                    _backNavigation.value = null
                }
    }

    private fun loadPersonSuggestions() {
        disposables.add(personsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .doOnSuccess { personSuggestions.addAll(it) }
                .map { it.map { it.name ?: "" } }
                .subscribe { suggestions -> _nameSuggestions.value = suggestions })
    }

    class Factory @Inject constructor(
            private val participant: Participant?,
            private val personsSource: PersonsSource,
            private val rxUtil: RxUtil,
            @ParticipantUi private val participantUiSubject: PublishSubject<Participant>)
        : ViewModelProvider.Factory
    {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                ParticipantViewModel(participant, personsSource, rxUtil,
                        participantUiSubject) as T
    }
}
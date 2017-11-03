package mydebts.android.app.feature.person

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class PersonViewModel(
        private val person: Person,
        private val participantsSource: ParticipantsSource,
        private val rxUtil: RxUtil) : ViewModel() {

    private var _name: MutableLiveData<String>? = null
    val name: LiveData<String>
        get() {
            if(_name == null) {
                _name = MutableLiveData()
                _name!!.value = person.name
            }
            return _name!!
        }

    private var _participants: MutableLiveData<List<Participant>>? = null
    val participants: LiveData<List<Participant>>
        get() {
            if (_participants == null) {
                _participants = MutableLiveData()
                loadEvents()
            }
            return _participants!!
        }

    private val disposables = CompositeDisposable()

    private fun loadEvents() {
        participantsSource.getByPersonId(person.id!!)
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { participants -> _participants!!.value = participants }
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory @Inject constructor(
            private val person: Person,
            private val participantsSource: ParticipantsSource,
            private val rxUtil: RxUtil) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PersonViewModel(person, participantsSource, rxUtil) as T
        }
    }
}

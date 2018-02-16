package mydebts.android.app.feature.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.ParticipantUi
import mydebts.android.app.extention.toEventDateString
import mydebts.android.app.rx.RxUtil
import mydebts.android.app.ui.ListEvent
import java.util.Calendar
import javax.inject.Inject

class EventViewModel constructor(
        private var event: Event?,
        private val eventsSource: EventsSource,
        private val personsSource: PersonsSource,
        private val participantsSource: ParticipantsSource,
        private val rxUtil: RxUtil,
        private @ParticipantUi val participantUiObservable: Observable<Participant>): ViewModel()
{
    private val calendar = Calendar.getInstance()
    private val disposables = CompositeDisposable()

    private val _title = MutableLiveData<CharSequence>()
    val title: LiveData<CharSequence>
        get() {
            _title.value = event?.date?.toEventDateString() ?: calendar.time.toEventDateString()
            return _title
        }

    private val _deleteMenuItemVisible = MutableLiveData<Boolean>()
    val deleteMenuItemVisible: LiveData<Boolean>
        get() {
            _deleteMenuItemVisible.value = event != null
            return _deleteMenuItemVisible
        }

    private val __participants = ArrayList<Participant>()
    private val _participants = MutableLiveData<Triple<List<Participant>?, ListEvent, Int?>>()
    val participants: LiveData<Triple<List<Participant>?, ListEvent, Int?>>
        get() {
            _participants.value = Triple<MutableList<Participant>?, ListEvent, Int?>(__participants, ListEvent.LIST_CHANGED, null)
            loadParticipants()
            disposables.add(participantUiObservable.subscribe { addParticipant(it) })
            return _participants
        }

    private val _navigation = MutableLiveData<Navigation>()
    internal val navigation: LiveData<Navigation?>
        get() = _navigation

    internal fun onParticipantClick(position: Int) {
        _navigation.value = Navigation(__participants[position])
        _navigation.value = null
    }

    internal fun onAddNewParticipantClick() {
        _navigation.value = Navigation(null)
        _navigation.value = null
    }

    private fun loadParticipants() {
        event?.id?.let {
            disposables.add(participantsSource.getByEventId(it)
                    .compose(rxUtil.singleSchedulersTransformer())
                    .doOnSuccess { __participants.clear(); __participants.addAll(it) }
                    .map { Triple<MutableList<Participant>?, ListEvent, Int?>(__participants, ListEvent.LIST_CHANGED, null) }
                    .subscribe { triple -> _participants.value = triple })
        }
    }

    private fun addParticipant(participant: Participant) {
        __participants.indexOfFirst { it.person == participant.person }
                .let {
                    if (it > -1) {
                        __participants[it].debt = participant.debt
                        _participants.value = Triple(null, ListEvent.ITEM_CHANGED, it)
                    } else {
                        __participants.add(participant)
                        _participants.value = Triple(null, ListEvent.ITEM_INSERTED, __participants.size - 1)
                    }
                }
    }

    class Factory @Inject constructor(
            private var event: Event?,
            private val eventsSource: EventsSource,
            private val personsSource: PersonsSource,
            private val participantsSource: ParticipantsSource,
            private val rxUtil: RxUtil,
            private @ParticipantUi val participantUiObservable: Observable<Participant>) : ViewModelProvider.Factory
    {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T
                = EventViewModel(event, eventsSource, personsSource, participantsSource,
                    rxUtil, participantUiObservable) as T
    }
}
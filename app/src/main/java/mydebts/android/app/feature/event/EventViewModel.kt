package mydebts.android.app.feature.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
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
            event?.date?.let { calendar.time = it }
            _title.value = calendar.time.toEventDateString()
            return _title
        }

    private val _dateNavigation = MutableLiveData<DateNavigation>()
    internal val dateNavigation: LiveData<DateNavigation>
        get() = _dateNavigation

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

    private val _participantNavigation = MutableLiveData<ParticipantNavigation>()
    internal val participantNavigation: LiveData<ParticipantNavigation>
        get() = _participantNavigation

    private val _backNavigation = MutableLiveData<BackNavigation>()
    internal val backNavigation: LiveData<BackNavigation>
        get() = _backNavigation

    override fun onCleared() {
        disposables.clear()
    }

    internal fun onParticipantClick(position: Int) {
        _participantNavigation.value = ParticipantNavigation(__participants[position])
        _participantNavigation.value = null
    }

    internal fun onAddNewParticipantClick() {
        _participantNavigation.value = ParticipantNavigation(null)
        _participantNavigation.value = null
    }

    internal fun onSetDateClick() {
        _dateNavigation.value = DateNavigation(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        _dateNavigation.value = null
    }

    internal fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        _title.value = calendar.time.toEventDateString()
    }

    internal fun onSaveEventClick() {
        disposables.add(Observable.combineLatest(eventObservable(), participantObservable(),
                BiFunction { event: Event, (id, _, person, debt): Participant ->
                    Participant(id, event, person, debt) })
                .flatMap {
                    val personSingle = it.person?.let {
                        it.takeIf { it.id != null }?.let { Single.just(it) }
                                ?: personsSource.insert(it)
                    } ?: Single.just(it.person)

                    val participant = it
                    personSingle.flatMap {
                        participant.person = it
                        participant.takeIf { it.id != null }?.let { participantsSource.update(it) }
                                ?: participantsSource.insert(participant)
                    }.toObservable()
                }
                .compose(rxUtil.observableSchedulersTransformer())
                .doOnComplete { _backNavigation.value = BackNavigation() }
                .subscribe())
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

    private fun eventObservable(): Observable<Event> {
        val date = calendar.time

        val eventSingle = event?.let { it.name = date.toEventDateString(); it.date = date; it }
                ?.let { eventsSource.update(it) }
                ?: eventsSource.insert(Event( name = date.toString(), date = date))

        return eventSingle.toObservable()
    }

    private fun participantObservable(): Observable<Participant> =
            Observable.fromIterable(__participants)

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
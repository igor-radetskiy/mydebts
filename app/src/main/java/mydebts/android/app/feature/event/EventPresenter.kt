package mydebts.android.app.feature.event

import android.view.View
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Participant
import mydebts.android.app.extention.toEventDateString
import mydebts.android.app.rx.RxUtil
import java.util.Calendar
import javax.inject.Inject

class EventPresenter @Inject constructor(
        private var event: Event?,
        private val screen: EventScreen,
        private val eventsSource: EventsSource,
        private val personsSource: PersonsSource,
        private val participantsSource: ParticipantsSource,
        private val rxUtil: RxUtil) {

    private val calendar = Calendar.getInstance()
    private val disposables = CompositeDisposable()

    private val participants = ArrayList<Participant>()

    fun onViewCreated() {
        screen.showTitle(event?.name ?: calendar.time.toEventDateString())

        event?.date?.let { calendar.time = it }

        event?.id?.let {
            disposables.add(participantsSource.getByEventId(it)
                    .compose(rxUtil.singleSchedulersTransformer())
                    .doOnSuccess { participants.clear(); participants.addAll(it) }
                    .subscribe{ _ -> handleParticipants() })
        }
    }

    fun onCreateOptionsMenu() {
        screen.setDeleteMenuItemVisible(event != null)
    }

    fun onDestroyView() {
        disposables.clear()
    }

    fun onSetDateClick() {
        screen.showDatePicker(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun onParticipantClick(position: Int) {
        screen.showParticipant(participants[position])
    }

    fun onAddNewParticipantClick() {
        screen.showNewParticipant()
    }

    fun onActionDeleteClick() {
        event?.let {
            disposables.add(eventsSource.delete(it)
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe { _ -> screen.navigateBack() })
        }
    }

    fun getParticipants(): MutableList<Participant> = participants

    fun addParticipant(participant: Participant) {
        participants.add(participant)
        screen.setEmptyViewVisibility(View.GONE)
        screen.setParticipantsViewVisibility(View.VISIBLE)
        screen.notifyParticipantInserted(participants.size - 1)
    }

    fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        screen.showTitle(calendar.time.toEventDateString())
    }

    fun onActionSaveClick() {
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
                .doOnComplete { screen.navigateBack() }
                .subscribe())
    }

    private fun handleParticipants() {
        if (participants.isEmpty()) {
            screen.setParticipantsViewVisibility(View.GONE)
            screen.setEmptyViewVisibility(View.VISIBLE)
        } else {
            screen.setParticipantsViewVisibility(View.VISIBLE)
            screen.setEmptyViewVisibility(View.GONE)
            screen.notifyParticipantsChanged()
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
            Observable.fromIterable(participants)
}
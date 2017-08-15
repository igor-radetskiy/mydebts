package mydebts.android.app.feature.event

import io.reactivex.functions.Consumer
import mydebts.android.app.R
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.res.Resources
import mydebts.android.app.rx.RxUtil
import java.util.*
import javax.inject.Inject

class EventViewModel @Inject constructor(
        var event: Event?,
        val screen: EventScreen,
        val resources: Resources,
        val eventsSource: EventsSource,
        val personsSource: PersonsSource,
        val participantsSource: ParticipantsSource,
        val rxUtil: RxUtil) {

    private val calendar = Calendar.getInstance()

    private lateinit var participants: MutableList<Participant>

    fun onViewCreated() {
        screen.showTitle(event?.name ?: resources.string(R.string.title_new_event))

        event?.date?.let { calendar.time = it }

        event?.id?.let { participantsSource.getByEventId(it)
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe(Consumer {
                    participants = it.toMutableList()
                    screen.showParticipants(participants) }) }
    }

    fun onCreateOptionsMenu() {
        screen.setDeleteMenuItemVisible(event != null)
    }

    fun onDestroyView() {

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
            eventsSource.delete(it)
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe { _ -> screen.navigateBack() }
        }
    }

    fun addParticipant(participant: Participant) {
        participants.add(participant)
        screen.showAddedParticipantAt(participants.size - 1)
    }

    fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        screen.showTitle(calendar.time.toString())
    }

    private fun saveEvent(participants: List<Participant>) {
        /*val date = Date()
        date.time = System.currentTimeMillis()

        val eventObservable = eventsSource.insert(Event(name = date.toString(), date = date))
                .toObservable()

        val participantObservable = Observable.fromIterable(participants)

        Observable.combineLatest(eventObservable, participantObservable,
                BiFunction { event: Event, participant: Participant ->
                    Participant(participant.id, event, participant.person, participant.debt)
                })
                .flatMap({ participant ->
                    personsSource.insert(participant.person!!)
                            .map { person ->
                                Participant(participant.id, participant.event, person, participant.debt)
                            }
                            .toObservable()
                })
                .flatMap({ participantsSource.insert(it)
                        .toObservable()
                })
                .compose(rxUtil.observableSchedulersTransformer())
                .doOnComplete({ (activity as MainRouter).navigateBack() })
                .subscribe()*/
    }
}
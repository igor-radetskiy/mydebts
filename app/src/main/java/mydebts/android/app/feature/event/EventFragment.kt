package mydebts.android.app.feature.event

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import java.util.Date

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import mydebts.android.app.R
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.feature.main.MainRouter
import mydebts.android.app.rx.RxUtil

class EventFragment : Fragment() {

    @Inject lateinit var rxUtil: RxUtil
    @Inject lateinit var eventsSource: EventsSource
    @Inject lateinit var personsSource: PersonsSource
    @Inject lateinit var participantsSource: ParticipantsSource

    private var adapter: ParticipantsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        (SubcomponentBuilderResolver.resolve(this) as EventSubcomponent.Builder)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.activity_event, container, false)

        val listParticipants = rootView.findViewById(R.id.list_participants) as RecyclerView
        listParticipants.layoutManager = LinearLayoutManager(listParticipants.context)

        adapter = ParticipantsAdapter()
        listParticipants.adapter = adapter

        if (arguments != null && arguments.containsKey(ARG_EVENT_ID)) {
            participantsSource.getByEventId(arguments.getLong(ARG_EVENT_ID))
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe(Consumer { adapter!!.setItems(it.toMutableList()) })
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_event, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_set_date -> return true
            R.id.action_save -> {
                saveEvent(adapter!!.getParticipants())
                return true
            }
            R.id.action_delete -> {
                deleteEvent()
                return true
            }
            else -> return false
        }
    }

    private fun saveEvent(participants: MutableList<Participant>) {
        participants
                .filter { TextUtils.isEmpty(it.person!!.name) || Math.abs(it.debt!!) < 0.001 }
                .forEach { participants.remove(it) }

        if (participants.isEmpty()) {
            return
        }

        val date = Date()
        date.time = System.currentTimeMillis()

        val eventObservable = eventsSource.insert(Event(name = date.toString(), date = date))
                .toObservable()

        val participantObservable = Observable.fromIterable(participants)

        Observable.combineLatest(eventObservable, participantObservable,
                BiFunction { event: Event, participant: Participant ->
                    Participant(participant.id, event, participant.person, participant.debt) })
                .flatMap({ participant ->
                    personsSource.insert(participant.person!!)
                            .map { person ->
                                Participant(participant.id, participant.event, person, participant.debt) }
                            .toObservable() })
                .flatMap({ participant ->
                    participantsSource.insert(participant)
                            .toObservable() })
                .compose(rxUtil.observableSchedulersTransformer())
                .doOnComplete({ (activity as MainRouter).navigateBack() })
                .subscribe()
    }

    private fun deleteEvent() {
        if (arguments.containsKey(ARG_EVENT_ID)) {
            eventsSource.delete(Event(arguments.getLong(ARG_EVENT_ID)))
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe { _ -> (activity as MainRouter).navigateBack() }
        } else {
            (activity as MainRouter).navigateBack()
        }
    }

    companion object {
        private val ARG_EVENT_ID = "ARG_EVENT_ID"

        fun newInstance(event: Event): EventFragment {
            val fragment = newInstance()
            fragment.arguments = Bundle()
            fragment.arguments.putLong(ARG_EVENT_ID, event.id!!)
            return fragment
        }

        fun newInstance(): EventFragment {
            return EventFragment()
        }
    }
}

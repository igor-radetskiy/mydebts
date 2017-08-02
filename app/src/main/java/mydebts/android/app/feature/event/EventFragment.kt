package mydebts.android.app.feature.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import mydebts.android.app.feature.participant.ParticipantActivity
import mydebts.android.app.rx.RxUtil

class EventFragment : Fragment() {

    @Inject lateinit var rxUtil: RxUtil
    @Inject lateinit var eventsSource: EventsSource
    @Inject lateinit var personsSource: PersonsSource
    @Inject lateinit var participantsSource: ParticipantsSource

    private lateinit var event: Event

    private lateinit var adapter: ParticipantsAdapter

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

        val buttonAddParticipant = rootView.findViewById(R.id.button_add_participant)
        buttonAddParticipant.setOnClickListener {
            startActivityForResult(ParticipantActivity.newIntent(activity), REQUEST_CODE_PARTICIPANT)
        }

        if (arguments != null && arguments.containsKey(ARG_EVENT)) {
            event = arguments.getParcelable(ARG_EVENT)
            activity.title = event.name
            participantsSource.getByEventId(event.id!!)
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe(Consumer { adapter.setParticipants(it) })
        } else {
            activity.setTitle(R.string.title_new_event)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_event, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_set_date -> return true
                R.id.action_save -> {
                    adapter.getParticipants()?.let { saveEvent(it) }
                    return true
                }
                R.id.action_delete -> {
                    deleteEvent()
                    return true
                }
                else -> { return false }
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PARTICIPANT && resultCode == Activity.RESULT_OK && data != null) {
            adapter.addParticipant(data.getParcelableExtra<Participant>(ParticipantActivity.EXTRA_PARTICIPANT))
        }
    }

    private fun saveEvent(participants: List<Participant>) {
        val date = Date()
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
                .flatMap({ participant ->
                    participantsSource.insert(participant)
                            .toObservable()
                })
                .compose(rxUtil.observableSchedulersTransformer())
                .doOnComplete({ (activity as MainRouter).navigateBack() })
                .subscribe()
    }

    private fun deleteEvent() {
        if (arguments.containsKey(ARG_EVENT)) {
            eventsSource.delete(Event(arguments.getLong(ARG_EVENT)))
                    .compose(rxUtil.singleSchedulersTransformer())
                    .subscribe { _ -> (activity as MainRouter).navigateBack() }
        } else {
            (activity as MainRouter).navigateBack()
        }
    }

    companion object {
        private val ARG_EVENT = "ARG_EVENT"

        private val REQUEST_CODE_PARTICIPANT = 0

        fun newInstance(event: Event): EventFragment {
            val fragment = newInstance()
            fragment.arguments = Bundle()
            fragment.arguments.putParcelable(ARG_EVENT, event)
            return fragment
        }

        fun newInstance(): EventFragment {
            return EventFragment()
        }
    }
}

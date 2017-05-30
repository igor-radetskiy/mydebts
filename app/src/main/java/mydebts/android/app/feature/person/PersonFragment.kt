package mydebts.android.app.feature.person

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mydebts.android.app.R
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class PersonFragment : Fragment() {

    @Inject lateinit var participantsSource: ParticipantsSource
    @Inject lateinit var rxUtil: RxUtil

    private lateinit var person: Person

    private lateinit var eventsListView: RecyclerView
    private lateinit var emptyView: View

    private val adapter: EventsAdapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        (SubcomponentBuilderResolver.resolve(this) as PersonSubcomonent.Builder)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_person, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        person = arguments.getParcelable(ARG_PERSON)
        activity.title = person.name

        eventsListView = view!!.findViewById(R.id.list_events) as RecyclerView
        emptyView = view.findViewById(R.id.text_no_events)

        eventsListView.layoutManager = LinearLayoutManager(activity.applicationContext)
        eventsListView.adapter = adapter
        participantsSource.getByPersonId(person.id!!)
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { participants -> setParticipants(participants) }
    }

    private fun setParticipants(participants: List<Participant>) {
        if (participants.isEmpty()) {
            eventsListView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            eventsListView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            adapter.participants = participants
        }
    }

    companion object {
        private val ARG_PERSON = "ARG_PERSON"

        fun newInstance(person: Person): PersonFragment {
            val fragment: PersonFragment = PersonFragment()
            fragment.arguments = Bundle()
            fragment.arguments.putParcelable(ARG_PERSON, person)
            return fragment
        }
    }
}
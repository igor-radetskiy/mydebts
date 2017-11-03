package mydebts.android.app.feature.person

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.SubcomponentBuilderResolver
import javax.inject.Inject

class PersonActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: PersonViewModel

    private lateinit var eventsListView: RecyclerView
    private lateinit var emptyView: View

    private val adapter: EventsAdapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        injectMembers()
        initViewModel()
    }

    private fun initUi() {
        setContentView(R.layout.activity_person)

        eventsListView = findViewById(R.id.list_events)
        emptyView = findViewById(R.id.text_no_events)

        eventsListView.layoutManager = LinearLayoutManager(applicationContext)
        eventsListView.adapter = adapter
    }

    private fun injectMembers() {
        (SubcomponentBuilderResolver.resolve(this) as PersonSubcomonent.Builder)
                .activity(this)
                .person(intent.getParcelableExtra(EXTRA_PERSON))
                .build().inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[PersonViewModel::class.java]

        viewModel.name.observe(this, Observer<String> { title = it })
        viewModel.participants.observe(this, Observer<List<Participant>> { handleParticipants(it) })
    }

    private fun handleParticipants(participants: List<Participant>?) {
        if (participants?.isEmpty() == true) {
            eventsListView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            eventsListView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            adapter.participants = participants
        }
    }

    companion object {
        private val EXTRA_PERSON = "mydebts.android.app.feature.person.extras.EXTRA_PERSON"

        fun newIntent(context: Context, person: Person): Intent {
            return Intent(context, PersonActivity::class.java)
                    .putExtra(EXTRA_PERSON, person)
        }
    }
}

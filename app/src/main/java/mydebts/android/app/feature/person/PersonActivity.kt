package mydebts.android.app.feature.person

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import dagger.android.AndroidInjection
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import javax.inject.Inject

class PersonActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: PersonViewModel

    private lateinit var eventsListView: RecyclerView
    private lateinit var emptyView: View

    private val adapter: EventsAdapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        AndroidInjection.inject(this)
        initViewModel()
    }

    private fun initUi() {
        setContentView(R.layout.activity_person)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        eventsListView = findViewById(R.id.list_events)
        emptyView = findViewById(R.id.text_no_events)

        eventsListView.layoutManager = LinearLayoutManager(applicationContext)
        eventsListView.adapter = adapter
    }

    private fun initViewModel() {
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when(item?.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> false
            }

    companion object {
        internal const val EXTRA_PERSON = "mydebts.android.app.feature.person.extras.EXTRA_PERSON"

        fun newIntent(context: Context, person: Person): Intent {
            return Intent(context, PersonActivity::class.java)
                    .putExtra(EXTRA_PERSON, person)
        }
    }
}

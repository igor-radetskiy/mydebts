package mydebts.android.app.ui.events

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dagger.android.AndroidInjection
import mydebts.android.app.R
import mydebts.android.app.feature.event.EventActivity
import mydebts.android.app.feature.events.EventsViewModel
import mydebts.android.app.feature.people.PeopleActivity
import java.util.ArrayList
import javax.inject.Inject

class EventsActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: EventsViewModel

    private val adapter = EventsAdapter()
    init {
        adapter.setOnItemClickListener { viewModel.onEventClick(it) }
    }

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var addEventButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setTitle(R.string.title_events)
        bindViews()
        AndroidInjection.inject(this)
        bindViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_events, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.action_switch_to_people -> {
                    startActivity(PeopleActivity.newIntent(this))
                    true
                }
                else -> false
            }

    private fun bindViews() {
        eventsRecyclerView = findViewById(R.id.list_events)
        eventsRecyclerView.adapter = adapter

        emptyView = findViewById(R.id.text_no_events)

        addEventButton = findViewById(R.id.button_add_event)
        addEventButton.setOnClickListener { viewModel.onAddEventClick() }
    }

    private fun bindViewModel() {
        viewModel.events.observe(this, Observer {
            adapter.items = it ?: ArrayList()
            handleItemsVisibility()
        })
        viewModel.addEventNavigation.observe(this, Observer {
            it?.let { startActivity(EventActivity.newIntent(this)) }
        })
        viewModel.eventNavigation.observe(this, Observer {
            it?.let { startActivity(EventActivity.newIntent(this, it)) }
        })
    }

    private fun handleItemsVisibility() {
        val isEmpty = adapter.itemCount == 0
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        eventsRecyclerView.visibility = if (!isEmpty) View.VISIBLE else View.GONE
    }
}

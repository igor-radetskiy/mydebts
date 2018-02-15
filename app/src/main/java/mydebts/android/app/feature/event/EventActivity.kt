package mydebts.android.app.feature.event

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dagger.android.AndroidInjection
import mydebts.android.app.R
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Participant
import mydebts.android.app.ui.ListEvent
import javax.inject.Inject

class EventActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: EventViewModel

    private val adapter = ParticipantsAdapter()

    private lateinit var emptyView: View
    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var addParticipantButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        bindViews()

        AndroidInjection.inject(this)

        bindViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        val deleteMenuItem = menu?.findItem(R.id.action_delete)
        viewModel.deleteMenuItemVisible.observe(this, Observer<Boolean> {
            it?.let { deleteMenuItem?.isVisible = it }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when(item?.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                R.id.action_set_date -> {
                    //viewModel.onSetDateClick()
                    true
                }
                R.id.action_save -> {
                    //viewModel.onActionSaveClick()
                    true
                }
                R.id.action_delete -> {
                    //viewModel.onActionDeleteClick()
                    true
                }
                else -> false
            }

    private fun bindViews() {
        emptyView = findViewById(R.id.text_no_participants)
        participantsRecyclerView = findViewById(R.id.list_participants)
        participantsRecyclerView.adapter = adapter
        addParticipantButton = findViewById(R.id.button_add_participant)
    }

    private fun bindViewModel() {
        viewModel.title.observe(this, Observer<CharSequence> { title = it })
        viewModel.participants.observe(this, Observer<Triple<List<Participant>?, ListEvent, Int?>> {
            when(it?.second) {
                ListEvent.LIST_CHANGED -> adapter.participants = it.first
                ListEvent.ITEM_INSERTED -> it.third?.let { adapter.notifyItemInserted(it) }
                ListEvent.ITEM_CHANGED -> it.third?.let { adapter.notifyItemChanged(it) }
            }
        })
    }

    companion object {

        val EXTRA_EVENT = "EXTRA_EVENT"

        fun newIntent(context: Context): Intent = Intent(context, EventActivity::class.java)

        fun newIntent(context: Context, event: Event): Intent =
                newIntent(context)
                        .putExtra(EXTRA_EVENT, event)
    }
}

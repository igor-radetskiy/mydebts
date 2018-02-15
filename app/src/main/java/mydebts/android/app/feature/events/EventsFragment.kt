package mydebts.android.app.feature.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import dagger.android.support.AndroidSupportInjection

import javax.inject.Inject

import mydebts.android.app.R
import mydebts.android.app.data.model.Event
import mydebts.android.app.feature.event.EventActivity
import mydebts.android.app.feature.people.PeopleActivity

class EventsFragment : Fragment(), ActionMode.Callback, EventsScreen {

    @Inject lateinit var presenter: EventsPresenter

    private lateinit var adapter: EventsAdapter

    private var eventsRecyclerView: RecyclerView? = null
    private var emptyView: View? = null

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        AndroidSupportInjection.inject(this)

        adapter = EventsAdapter(presenter.getEvents(), presenter.getSelections())

        adapter.setOnItemClickListener {
            if (actionMode == null) {
                presenter.onEventClick(it)
            } else {
                presenter.onSelectEvent(it)
            }
        }

        adapter.setOnItemLongClickListener { position ->
            actionMode.takeIf { actionMode == null }?.let {
                presenter.onSelectEvent(position)
                actionMode = activity.startActionMode(this)
                true
            } == true
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        activity.setTitle(R.string.title_events)

        view?.let {
            eventsRecyclerView = view.findViewById(R.id.list_events)
            eventsRecyclerView?.adapter = adapter

            emptyView = view.findViewById(R.id.text_no_events)

            val addEventButton: View = view.findViewById(R.id.button_add_event)
            addEventButton.setOnClickListener { startActivity(EventActivity.newIntent(activity)) }
        }

        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_events, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = item?.let {
        when (it.itemId) {
            R.id.action_switch_to_people -> {
                startActivity(PeopleActivity.newIntent(activity))
                true
            }
            else -> false
        }
    } == true

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = item?.let {
        when (it.itemId) {
            R.id.action_delete -> {
                presenter.deleteSelectedEvents()
                mode?.finish()
                true
            }
            else -> false
        }
    } == true

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.context_menu_event, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        presenter.clearSelections()
    }

    override fun showEvent(event: Event) {
        startActivity(EventActivity.newIntent(activity, event))
    }

    override fun setEventsVisibility(visibility: Int) {
        eventsRecyclerView?.visibility = visibility
    }

    override fun setEmptyViewVisibility(visibility: Int) {
        emptyView?.visibility = visibility
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun notifyItemRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }
}

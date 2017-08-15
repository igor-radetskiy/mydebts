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

import javax.inject.Inject

import mydebts.android.app.R
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.feature.main.MainRouter
import mydebts.android.app.rx.RxUtil

class EventsFragment : Fragment(), ActionMode.Callback {

    @Inject lateinit var eventsSource: EventsSource
    @Inject lateinit var rxUtil: RxUtil

    internal var eventsRecyclerView: RecyclerView? = null
    internal var emptyView: View? = null

    internal var actionMode: ActionMode? = null

    internal val adapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        (SubcomponentBuilderResolver.resolve(this) as EventsSubcomponent.Builder)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        activity.setTitle(R.string.title_events)

        ViewBinder.bind(this)

        eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { events -> setEvents(events) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_events, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_switch_to_persons -> {
                (activity as MainRouter).navigateToPersons()
                return true
            }
            else -> return false
        }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_delete -> {
                eventsSource.delete(adapter.getSelections())
                        .compose(rxUtil.singleSchedulersTransformer())
                        .subscribe { events -> events.forEach { adapter.remove(it) }}
                mode?.finish()
                return true
            }
            else -> return false
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.context_menu_event, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        adapter.clearSelections()
    }

    internal fun onAddEventClick() {
        (activity as MainRouter).navigateToNewEvent()
    }

    internal fun onItemClick(position: Int) {
        if (actionMode == null) {
            (activity as MainRouter).navigateToEvent(adapter.events!![position])
        } else {
            adapter.selectItem(position)
        }
    }

    internal fun onItemLongClick(position: Int): Boolean {
        if (actionMode != null) {
            return false
        }

        adapter.selectItem(position)
        actionMode = activity.startActionMode(this)
        return true
    }

    private fun setEvents(events: List<Event>) {
        emptyView?.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        eventsRecyclerView?.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE

        adapter.events = events
    }
}

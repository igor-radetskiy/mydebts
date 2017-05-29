package mydebts.android.app.feature.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*

import javax.inject.Inject

import mydebts.android.app.R
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.di.SubcomponentBuilderResolver
import mydebts.android.app.feature.main.MainRouter
import mydebts.android.app.rx.RxUtil

class EventsFragment : Fragment() {

    @Inject lateinit var eventsSource: EventsSource
    @Inject lateinit var rxUtil: RxUtil

    internal var eventsRecyclerView: RecyclerView? = null
    internal var emptyView: View? = null

    internal var adapter: EventsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        (SubcomponentBuilderResolver.resolve(this) as EventsSubcomponent.Builder)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_events, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        ViewBinder.bind(this)

        eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe({ this.setEvents(it) }, { this.setError(it) })
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

    internal fun onAddEventClick() {
        (activity as MainRouter).navigateToNewEvent()
    }

    internal fun onEventClick(event: Event) {
        (activity as MainRouter).navigateToEvent(event)
    }

    private fun setEvents(events: List<Event>) {
        emptyView?.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        eventsRecyclerView?.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE

        adapter?.setEvents(events)
    }

    private fun setError(throwable: Throwable) {

    }
}

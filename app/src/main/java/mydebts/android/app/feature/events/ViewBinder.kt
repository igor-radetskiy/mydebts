package mydebts.android.app.feature.events

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import mydebts.android.app.R
import mydebts.android.app.ui.OnItemClickListener
import mydebts.android.app.ui.OnItemLongClickListener

internal object ViewBinder {
    fun bind(fragment: EventsFragment) {
        val rootView = fragment.view ?: return

        rootView.findViewById(R.id.button_add_event)
                .setOnClickListener { _ -> fragment.onAddEventClick() }

        fragment.adapter.setOnItemClickListener(object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                fragment.onItemClick(position)
            }
        })
        fragment.adapter.setOnItemLongClickListener(object: OnItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                return fragment.onItemLongClick(position)
            }
        })

        fragment.eventsRecyclerView = rootView.findViewById(R.id.list_events) as RecyclerView
        fragment.eventsRecyclerView?.layoutManager = LinearLayoutManager(rootView.context)
        fragment.eventsRecyclerView?.adapter = fragment.adapter

        fragment.emptyView = rootView.findViewById(R.id.text_no_events)
    }
}

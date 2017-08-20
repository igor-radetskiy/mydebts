package mydebts.android.app.feature.events

import mydebts.android.app.data.model.Event

interface EventsScreen {

    fun showEvent(event: Event)

    fun setEventsVisibility(visibility: Int)

    fun setEmptyViewVisibility(visibility: Int)

    fun notifyDataSetChanged()

    fun notifyItemChanged(position: Int)

    fun notifyItemRemoved(position: Int)
}
package mydebts.android.app.feature.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import mydebts.android.app.R
import mydebts.android.app.data.model.Event

internal class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    private var events: List<Event>? = null
    private var onEventClickListener: OnEventClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder.create(parent)

        viewHolder.itemView.setOnClickListener { _ ->
            val position = viewHolder.adapterPosition
            if (onEventClickListener != null && position >= 0) {
                onEventClickListener!!.onEventClick(events!![position])
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.date.text = events!![position].date!!.toString()
    }

    override fun getItemCount(): Int {
        return if (events == null) 0 else events!!.size
    }

    fun setEvents(events: List<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    fun setOnEventClickListener(listener: OnEventClickListener) {
        this.onEventClickListener = listener
    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.text1) as TextView

        companion object {

            fun create(parent: ViewGroup): EventViewHolder {
                return EventViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_one_line, parent, false))
            }
        }
    }

    interface OnEventClickListener {
        fun onEventClick(event: Event)
    }
}

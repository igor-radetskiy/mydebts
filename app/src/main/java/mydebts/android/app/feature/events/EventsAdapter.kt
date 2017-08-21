package mydebts.android.app.feature.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import mydebts.android.app.R
import mydebts.android.app.data.model.Event
import mydebts.android.app.extention.toEventDateString

internal class EventsAdapter(
        private val events: List<Event>,
        private val selections: List<Int>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemLongClickListener: ((Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder.create(parent)
        viewHolder.itemView.setOnClickListener { onItemClickListener?.invoke(viewHolder.adapterPosition) }
        viewHolder.itemView.setOnLongClickListener { onItemLongClickListener?.invoke(viewHolder.adapterPosition) ?: false }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.date.text = events[position].date?.toEventDateString()
        holder.itemView.isSelected = selections.contains(position)
    }

    override fun getItemCount(): Int = events.size

    fun setOnItemClickListener(listener: ((Int) -> Unit)) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: ((Int) -> Boolean)) {
        onItemLongClickListener = listener
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
}

package mydebts.android.app.ui.events

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import mydebts.android.app.R
import mydebts.android.app.data.model.Event
import mydebts.android.app.extention.toEventDateString
import java.util.ArrayList

internal class EventsAdapter: RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    private var _items = ArrayList<Event>()
    internal var items: List<Event>
        get() = _items
        set(value) {
            val diffResult = DiffUtil.calculateDiff(EventsDiffCallback(_items, value))
            _items.clear()
            _items.addAll(value)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder.create(parent)
        viewHolder.itemView.setOnClickListener { onItemClickListener?.invoke(viewHolder.adapterPosition) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        _items.takeIf { position >= 0 && position < it.size }
                ?.let{ holder.date.text = it[position].date?.toEventDateString() }
    }

    override fun getItemCount(): Int = _items.size

    fun setOnItemClickListener(listener: ((Int) -> Unit)) {
        onItemClickListener = listener
    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.text1)

        companion object {

            fun create(parent: ViewGroup): EventViewHolder {
                return EventViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_one_line, parent, false))
            }
        }
    }
}

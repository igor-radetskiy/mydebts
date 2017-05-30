package mydebts.android.app.feature.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import mydebts.android.app.R
import mydebts.android.app.data.model.Event
import mydebts.android.app.ui.OnItemClickListener
import mydebts.android.app.ui.OnItemLongClickListener

internal class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    private var _events: MutableList<Event>? = null
    var events: List<Event>?
        get() = _events
        set(value) {
            _events = value?.toMutableList()
            notifyDataSetChanged()
        }

    private val selections: MutableList<Int> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val viewHolder = EventViewHolder.create(parent)
        viewHolder.itemView.setOnClickListener { onItemClickListener?.onItemClick(viewHolder.adapterPosition) }
        viewHolder.itemView.setOnLongClickListener { onItemLongClickListener?.onItemLongClick(viewHolder.adapterPosition) ?: false }
        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.date.text = _events!![position].date!!.toString()
        holder.itemView.isSelected = selections.contains(position)
    }

    override fun getItemCount(): Int {
        return _events?.size ?: 0
    }

    fun remove(event: Event) {
        val index: Int = _events?.indexOf(event) ?: -1
        if (index >= 0) {
            _events?.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getSelections(): List<Event> {
        return selections.mapTo(ArrayList()) { _events!![it] }
    }

    fun selectItem(position: Int) {
        if (selections.contains(position)) {
            selections.remove(position)
        } else {
            selections.add(position)
        }
        notifyItemChanged(position)
    }

    fun clearSelections() {
        selections.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
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

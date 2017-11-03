package mydebts.android.app.feature.person

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.extention.setCurrencyText
import mydebts.android.app.extention.toEventDateString

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private var _participants: List<Participant>? = null
    var participants: List<Participant>?
        get() = _participants
        set(value) {
            _participants = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val participant: Participant? = _participants?.get(position)
        holder?.eventName?.text = participant?.event?.date?.toEventDateString()
        holder?.price?.setCurrencyText(participant?.debt!!)
    }

    override fun getItemCount(): Int {
        return _participants?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val eventName: TextView = itemView.findViewById(R.id.text1)
        val price: TextView = itemView.findViewById(R.id.text2)

        companion object {
            fun create(parent: ViewGroup?) : ViewHolder {
                return ViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_two_spans, parent, false))
            }
        }
    }
}
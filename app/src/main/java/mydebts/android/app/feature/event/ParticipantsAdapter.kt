package mydebts.android.app.feature.event

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.extention.toCurrencyString

internal class ParticipantsAdapter: RecyclerView.Adapter<ParticipantsAdapter.EventViewHolder>() {

    private var _participants: List<Participant>? = null
    internal var participants: List<Participant>?
        get() = _participants
        set(value) { _participants = value; notifyDataSetChanged() }

    private var onParticipantClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val holder = EventViewHolder.create(parent)

        holder.itemView.setOnClickListener {
            onParticipantClickListener?.let { it(holder.adapterPosition) }
        }

        return holder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        participants?.get(position).let {
            holder.name.text = it?.person?.name.takeUnless { TextUtils.isEmpty(it) } ?: "Unknown"
            holder.debt.text = it?.debt?.toCurrencyString()  ?: .0.toCurrencyString()
        }
    }

    override fun getItemCount(): Int = participants?.size ?: 0

    fun setOnParticipantClickListener(l: (Int) -> Unit) {
        onParticipantClickListener = l
    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text1)
        val debt: TextView = itemView.findViewById(R.id.text2)

        companion object {

            fun create(parent: ViewGroup): EventViewHolder {
                return EventViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_two_spans, parent, false))
            }
        }
    }
}

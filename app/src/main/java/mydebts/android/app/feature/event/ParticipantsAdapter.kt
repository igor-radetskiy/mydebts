package mydebts.android.app.feature.event

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.Locale

import mydebts.android.app.R
import mydebts.android.app.data.model.Participant

internal class ParticipantsAdapter : RecyclerView.Adapter<ParticipantsAdapter.EventViewHolder>() {
    private var participants: MutableList<Participant>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        participants?.get(position)?.let {

            holder.name.text = it.person?.name.takeUnless { TextUtils.isEmpty(it) } ?: "Unknown"

            holder.debt.text = it.debt?.takeUnless { Math.abs(it) < 0.001 }
                    ?.let { String.format(Locale.getDefault(), "%f", it) } ?: "0"
        }
    }

    override fun getItemCount(): Int {
        return participants?.size ?: 0
    }

    fun getParticipants(): List<Participant>? {
        return participants
    }

    fun getParticipant(position: Int): Participant? {
        return participants?.get(position)
    }

    fun setParticipants(participants: List<Participant>) {
        this.participants = participants.toMutableList()
        notifyDataSetChanged()
    }

    fun removeParticipantAt(position: Int) {
        participants?.let {
            it.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addParticipant(participant: Participant) {
        val list = participants ?: ArrayList()

        list.add(participant)
        notifyItemInserted(list.size - 1)

        participants = list
    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text1) as TextView
        val debt: TextView = itemView.findViewById(R.id.text2) as TextView

        companion object {

            fun create(parent: ViewGroup): EventViewHolder {
                return EventViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_two_spans, parent, false))
            }
        }
    }
}

package mydebts.android.app.feature.event

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import java.util.ArrayList
import java.util.Locale

import mydebts.android.app.R
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person

internal class ParticipantsAdapter : RecyclerView.Adapter<ParticipantsAdapter.EventViewHolder>() {
    private var participants: MutableList<Participant> = ArrayList()

    init {
        insertNewEmptyItem()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val holder = EventViewHolder.create(parent)

        val emptyTextWatcher = EmptyTextWatcher(this, holder)

        holder.name.addTextChangedListener(NameTextWatcher(this, holder))
        holder.name.addTextChangedListener(emptyTextWatcher)

        holder.price.addTextChangedListener(PriceTextWatcher(this, holder))
        holder.price.addTextChangedListener(emptyTextWatcher)

        return holder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val participant = participants[position]

        if (participant.person != null && !TextUtils.isEmpty(participant.person!!.name)) {
            holder.name.setText(participant.person!!.name)
        }

        val debt = if (Math.abs(participant.debt!!) < 0.001)
            ""
        else
            String.format(Locale.getDefault(), "%f", participant.debt)
        if (!TextUtils.isEmpty(debt)) {
            holder.price.setText(debt)
        }
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    fun getParticipants(): MutableList<Participant> {
        return participants
    }

    fun getItem(position: Int): Participant {
        return participants[position]
    }

    fun setItems(participants: MutableList<Participant>) {
        this.participants = participants
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        participants.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertNewEmptyItem() {
        participants.add(Participant( person = Person(), debt = 0.0))
        notifyItemInserted(participants.size - 1)
    }

    fun updateItemName(position: Int, name: String) {
        val participant = participants[position]
        participant.person!!.name = name
    }

    fun updateItemPrice(position: Int, debt: Double) {
        val participant = participants[position]
        participant.debt = debt
    }

    internal class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: EditText = itemView.findViewById(R.id.name) as EditText
        val price: EditText = itemView.findViewById(R.id.price) as EditText

        companion object {

            fun create(parent: ViewGroup): EventViewHolder {
                return EventViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_participant, parent, false))
            }
        }
    }
}

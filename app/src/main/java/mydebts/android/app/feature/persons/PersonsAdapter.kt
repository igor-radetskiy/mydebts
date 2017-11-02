package mydebts.android.app.feature.persons

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mydebts.android.app.R
import mydebts.android.app.data.model.Person

internal class PersonsAdapter : RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    private var _persons: List<Person>? = null
    var persons: List<Person>?
        get() = _persons
        set(value) {
            _persons = value
            notifyDataSetChanged()
        }

    private var onPersonClickListener: OnPersonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder.create(parent)
        viewHolder.itemView.setOnClickListener { _ ->
            val position = viewHolder.adapterPosition
            if (position >= 0) {
                onPersonClickListener?.onPersonClick(_persons!![position])
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.name?.text = _persons?.get(position)?.name
    }

    override fun getItemCount(): Int {
        return _persons?.size ?: 0
    }

    fun setOnPersonClickListener(onPersonClickListener: OnPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.text1)

        companion object {

            fun create(parent: ViewGroup?) : ViewHolder {
                return ViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_one_line, parent, false))
            }
        }
    }

    interface OnPersonClickListener {
        fun onPersonClick(person: Person)
    }
}

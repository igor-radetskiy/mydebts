package mydebts.android.app.ui.events

import android.support.v7.util.DiffUtil
import mydebts.android.app.data.model.Event

class EventsDiffCallback constructor(
        private val oldList: List<Event>,
        private val newList: List<Event>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].date?.time == newList[newItemPosition].date?.time

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            newList[newItemPosition].id == oldList[oldItemPosition].id
}
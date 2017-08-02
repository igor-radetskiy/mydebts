package mydebts.android.app.feature.event

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

import java.lang.ref.WeakReference


internal class EmptyTextWatcher(adapter: ParticipantsAdapter, holder: ParticipantsAdapter.EventViewHolder) : TextWatcher {
    private val adapterRef: WeakReference<ParticipantsAdapter> = WeakReference(adapter)
    private val holderRef: WeakReference<ParticipantsAdapter.EventViewHolder> = WeakReference(holder)

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val adapter = adapterRef.get()
        val holder = holderRef.get()

        if (adapter == null || holder == null) {
            return
        }

        /*val participant = adapter.getItem(holder.adapterPosition)
        if (participant.person != null && TextUtils.equals(participant.person!!.name, holder.name.text)) {
            return
        }

        if (TextUtils.isEmpty(holder.name.text)
                && TextUtils.isEmpty(holder.debt.text)
                && adapter.itemCount > 1) {
            adapter.removeItem(holder.adapterPosition)
        } else if (!TextUtils.isEmpty(holder.name.text)
                && !TextUtils.isEmpty(holder.debt.text)
                && holder.adapterPosition == adapter.itemCount - 1) {
            adapter.insertNewEmptyItem()
        }*/
    }

    override fun afterTextChanged(s: Editable) {}
}

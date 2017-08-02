package mydebts.android.app.feature.event

import android.text.Editable
import android.text.TextWatcher

import java.lang.ref.WeakReference

internal class NameTextWatcher(adapter: ParticipantsAdapter, holder: ParticipantsAdapter.EventViewHolder) : TextWatcher {
    private val adapterRef: WeakReference<ParticipantsAdapter> = WeakReference(adapter)
    private val holderRef: WeakReference<ParticipantsAdapter.EventViewHolder> = WeakReference(holder)

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        val adapter = adapterRef.get()
        val holder = holderRef.get()

        if (adapter == null || holder == null) {
            return
        }

        //adapter.updateItemName(holder.adapterPosition, charSequence.toString())
    }

    override fun afterTextChanged(editable: Editable) {}
}

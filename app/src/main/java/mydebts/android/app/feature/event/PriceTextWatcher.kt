package mydebts.android.app.feature.event

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

import java.lang.ref.WeakReference

internal class PriceTextWatcher(adapter: ParticipantsAdapter, holder: ParticipantsAdapter.EventViewHolder) : TextWatcher {
    private val adapterRef: WeakReference<ParticipantsAdapter> = WeakReference(adapter)
    private val holderRef: WeakReference<ParticipantsAdapter.EventViewHolder> = WeakReference(holder)

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    override fun onTextChanged(text: CharSequence, i: Int, i1: Int, i2: Int) {
        var charSequence = text
        val adapter = adapterRef.get()
        val holder = holderRef.get()

        if (adapter == null || holder == null
                || TextUtils.equals(charSequence, "$")
                || charSequence.isEmpty()) {
            return
        }

        if (charSequence[0] == '$') {
            charSequence = charSequence.subSequence(1, charSequence.length - 1)
        }

        if (charSequence.isNotEmpty()) {
            val price = java.lang.Double.parseDouble(charSequence.toString())
            adapter.updateItemPrice(holder.adapterPosition, price)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (s != null && s.isNotEmpty() && s[0] != '$') {
            s.insert(0, "$")
        }
    }
}

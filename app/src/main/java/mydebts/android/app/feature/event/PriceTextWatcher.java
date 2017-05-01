package mydebts.android.app.feature.event;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.lang.ref.WeakReference;

class PriceTextWatcher implements TextWatcher {
    private final WeakReference<ParticipantsAdapter> adapterRef;
    private final WeakReference<ParticipantsAdapter.EventViewHolder> holderRef;

    PriceTextWatcher(ParticipantsAdapter adapter, ParticipantsAdapter.EventViewHolder holder) {
        adapterRef = new WeakReference<>(adapter);
        holderRef = new WeakReference<>(holder);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ParticipantsAdapter adapter = adapterRef.get();
        ParticipantsAdapter.EventViewHolder holder = holderRef.get();

        if (adapter == null || holder == null
                || (TextUtils.equals(charSequence, "$"))
                || charSequence.length() < 1) {
            return;
        }

        if (charSequence.charAt(0) == '$') {
            charSequence = charSequence.subSequence(1, charSequence.length() - 1);
        }

        if (charSequence.length() > 0) {
            double price = Double.parseDouble(charSequence.toString());
            adapter.updateItemPrice(holder.getAdapterPosition(), price);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.length() > 0 && s.charAt(0) != '$') {
            s.insert(0, "$");
        }
    }
}

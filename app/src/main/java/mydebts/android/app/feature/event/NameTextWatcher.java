package mydebts.android.app.feature.event;

import android.text.Editable;
import android.text.TextWatcher;

import java.lang.ref.WeakReference;

class NameTextWatcher implements TextWatcher {
    private final WeakReference<ParticipantsAdapter> adapterRef;
    private final WeakReference<ParticipantsAdapter.EventViewHolder> holderRef;

    NameTextWatcher(ParticipantsAdapter adapter, ParticipantsAdapter.EventViewHolder holder) {
        adapterRef = new WeakReference<>(adapter);
        holderRef = new WeakReference<>(holder);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ParticipantsAdapter adapter = adapterRef.get();
        ParticipantsAdapter.EventViewHolder holder = holderRef.get();

        if (adapter == null || holder == null) {
            return;
        }

        adapter.updateItemName(holder.getAdapterPosition(), charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {}
}

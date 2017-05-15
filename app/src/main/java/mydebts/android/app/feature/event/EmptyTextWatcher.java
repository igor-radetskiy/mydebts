package mydebts.android.app.feature.event;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.lang.ref.WeakReference;

import mydebts.android.app.data.model.Participant;

class EmptyTextWatcher implements TextWatcher {
    private final WeakReference<ParticipantsAdapter> adapterRef;
    private final WeakReference<ParticipantsAdapter.EventViewHolder> holderRef;

    EmptyTextWatcher(ParticipantsAdapter adapter, ParticipantsAdapter.EventViewHolder holder) {
        adapterRef = new WeakReference<>(adapter);
        holderRef = new WeakReference<>(holder);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ParticipantsAdapter adapter = adapterRef.get();
        ParticipantsAdapter.EventViewHolder holder = holderRef.get();

        if (adapter == null || holder == null) {
            return;
        }

        Participant participant = adapter.getItem(holder.getAdapterPosition());
        if (participant.getPerson() != null
                && TextUtils.equals(participant.getPerson().getName(), holder.name.getText())) {
            return;
        }

        if (TextUtils.isEmpty(holder.name.getText())
                && TextUtils.isEmpty(holder.price.getText())
                && adapter.getItemCount() > 1) {
            adapter.removeItem(holder.getAdapterPosition());
        } else if (!TextUtils.isEmpty(holder.name.getText())
                && !TextUtils.isEmpty(holder.price.getText())
                && holder.getAdapterPosition() == adapter.getItemCount() - 1) {
            adapter.insertNewEmptyItem();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}

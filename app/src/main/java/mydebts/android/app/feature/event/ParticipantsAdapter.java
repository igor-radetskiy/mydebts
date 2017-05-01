package mydebts.android.app.feature.event;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import mydebts.android.app.R;
import mydebts.android.app.db.Participant;
import mydebts.android.app.db.Person;

class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.EventViewHolder> {
    private List<Participant> participants = new ArrayList<>();

    ParticipantsAdapter() {
        insertNewEmptyItem();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventViewHolder holder = EventViewHolder.create(parent);

        TextWatcher emptyTextWatcher = new EmptyTextWatcher(this, holder);

        holder.name.addTextChangedListener(new NameTextWatcher(this, holder));
        holder.name.addTextChangedListener(emptyTextWatcher);

        holder.price.addTextChangedListener(new PriceTextWatcher(this, holder));
        holder.price.addTextChangedListener(emptyTextWatcher);

        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return participants.size();
    }

    List<Participant> getParticipants() {
        return participants;
    }

    void removeItem(int position) {
        participants.remove(position);
        notifyItemRemoved(position);
    }

    void insertNewEmptyItem() {
        Participant participant = new Participant();
        participant.setPerson(new Person());
        participant.setDebt(0);

        participants.add(participant);
        notifyItemInserted(participants.size() - 1);
    }

    void updateItemName(int position, String name) {
        Participant participant = participants.get(position);
        participant.peakPerson().setName(name);
    }

    void updateItemPrice(int position, double debt) {
        Participant participant = participants.get(position);
        participant.setDebt(debt);
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        final EditText name;
        final EditText price;

        EventViewHolder(View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.name);
            price = (EditText) itemView.findViewById(R.id.price);
        }

        static EventViewHolder create(ViewGroup parent) {
            return new EventViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_participant, parent, false));
        }
    }
}

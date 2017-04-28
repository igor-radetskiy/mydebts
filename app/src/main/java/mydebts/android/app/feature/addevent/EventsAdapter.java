package mydebts.android.app.feature.addevent;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mydebts.android.app.R;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    int itemCount = 1;

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventViewHolder holder = EventViewHolder.create(parent);

        TextWatcher emptyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(holder.name.getText())
                        && TextUtils.isEmpty(holder.price.getText())
                        && itemCount > 1) {
                    notifyItemRemoved(holder.getAdapterPosition());
                    itemCount--;
                } else if (!TextUtils.isEmpty(holder.name.getText())
                        && !TextUtils.isEmpty(holder.price.getText())
                        && holder.getAdapterPosition() == itemCount - 1) {
                    notifyItemInserted(itemCount++);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        holder.name.addTextChangedListener(emptyTextWatcher);
        holder.price.addTextChangedListener(emptyTextWatcher);
        holder.price.addTextChangedListener(new MoneyTextWatcher());

        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemCount;
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
                    .inflate(R.layout.item_event, parent, false));
        }
    }
}

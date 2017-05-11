package mydebts.android.app.feature.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mydebts.android.app.R;
import mydebts.android.app.data.model.Event;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<Event> events;
    private OnEventClickListener listener;

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventViewHolder viewHolder = EventViewHolder.create(parent);

        viewHolder.itemView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            if (listener != null && position >= 0) {
                listener.onEventClick(events.get(position));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.date.setText(events.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }

    void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView date;

        EventViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
        }

        static EventViewHolder create(ViewGroup parent) {
            return new EventViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event, parent, false));
        }
    }

    interface OnEventClickListener {
        void onEventClick(Event event);
    }
}

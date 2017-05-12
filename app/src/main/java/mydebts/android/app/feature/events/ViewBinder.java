package mydebts.android.app.feature.events;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mydebts.android.app.R;

class ViewBinder {
    static void bind(EventsFragment fragment) {
        View rootView = fragment.getView();

        if (rootView == null) {
            return;
        }

        rootView.findViewById(R.id.button_add_event)
                .setOnClickListener(v -> fragment.onAddEventClick());

        fragment.adapter = new EventsAdapter();
        fragment.adapter.setOnEventClickListener(fragment::onEventClick);

        fragment.eventsRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_events);
        fragment.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        fragment.eventsRecyclerView.setAdapter(fragment.adapter);

        fragment.emptyView = rootView.findViewById(R.id.text_no_events);
    }
}

package mydebts.android.app.feature.events;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import dagger.Module;
import dagger.Provides;
import mydebts.android.app.R;
import mydebts.android.app.di.ActivityModule;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.feature.main.MainActivity;

@Module
public class EventsUiModule implements ActivityModule<MainActivity> {
    private final EventsFragment fragment;
    private final View rootView;

    EventsUiModule(@NonNull EventsFragment fragment, @NonNull View rootView) {
        this.fragment = fragment;
        this.rootView = rootView;

        rootView.findViewById(R.id.button_add_event)
                .setOnClickListener(v -> fragment.onAddEventClick());
    }

    @ActivityScope
    @Provides
    View provideEmptyView() {
        return rootView.findViewById(R.id.text_no_events);
    }

    @ActivityScope
    @Provides
    RecyclerView provideEventsRecyclerView(EventsAdapter adapter) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @ActivityScope
    @Provides
    EventsAdapter provideEventsAdapter() {
        EventsAdapter adapter = new EventsAdapter();
        adapter.setOnEventClickListener(fragment::onEventClick);
        return adapter;
    }
}

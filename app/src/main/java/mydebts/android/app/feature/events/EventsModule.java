package mydebts.android.app.feature.events;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import dagger.Module;
import dagger.Provides;
import mydebts.android.app.R;
import mydebts.android.app.db.DaoSession;
import mydebts.android.app.db.EventDao;
import mydebts.android.app.di.ActivityModule;
import mydebts.android.app.di.ActivityScope;

@Module
public class EventsModule implements ActivityModule<EventsActivity> {
    private final EventsActivity activity;

    EventsModule(EventsActivity activity) {
        this.activity = activity;

        activity.findViewById(R.id.button_add_event)
                .setOnClickListener(v -> activity.onAddEventClick());
    }

    @ActivityScope
    @Provides
    EventDao provideEventDao(DaoSession daoSession) {
        return daoSession.getEventDao();
    }

    @ActivityScope
    @Provides
    View provideEmptyView() {
        return activity.findViewById(R.id.text_no_events);
    }

    @ActivityScope
    @Provides
    RecyclerView provideEventsRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.list_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        return recyclerView;
    }
}

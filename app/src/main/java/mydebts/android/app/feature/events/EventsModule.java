package mydebts.android.app.feature.events;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

        activity.findViewById(R.id.activity_events_add)
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
        TextView emptyTextView =
                (TextView) activity.findViewById(R.id.text_empty);

        emptyTextView.setText(R.string.events_text_no_events);

        return emptyTextView;
    }

    @ActivityScope
    @Provides
    RecyclerView provideEventsRecyclerView() {
        RecyclerView recyclerView =
                (RecyclerView) activity.findViewById(R.id.list);

        return recyclerView;
    }
}

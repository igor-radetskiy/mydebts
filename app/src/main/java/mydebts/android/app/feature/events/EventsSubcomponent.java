package mydebts.android.app.feature.events;

import dagger.Subcomponent;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.di.ActivitySubcomponent;

@ActivityScope
@Subcomponent(modules = EventsModule.class)
public interface EventsSubcomponent extends ActivitySubcomponent<EventsActivity> {

    void inject(EventsActivity activity);

    @Subcomponent.Builder
    interface Builder extends ActivitySubcomponent.Builder {

        Builder module(EventsModule module);

        EventsSubcomponent build();
    }
}

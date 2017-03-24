package mydebts.android.app.feature.events;

import dagger.Subcomponent;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.di.ActivitySubcomponent;
import mydebts.android.app.di.SubcomponentBuilder;

@ActivityScope
@Subcomponent(modules = EventsModule.class)
public interface EventsSubcomponent extends ActivitySubcomponent<EventsActivity> {

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<EventsModule, EventsSubcomponent> {}
}

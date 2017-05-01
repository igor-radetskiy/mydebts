package mydebts.android.app.feature.events;

import dagger.Subcomponent;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.di.SubcomponentBuilder;

@ActivityScope
@Subcomponent(modules = EventsUiModule.class)
public interface EventsSubcomponent {

    void inject(EventsFragment fragment);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder {

        Builder module(EventsUiModule module);

        EventsSubcomponent build();
    }
}

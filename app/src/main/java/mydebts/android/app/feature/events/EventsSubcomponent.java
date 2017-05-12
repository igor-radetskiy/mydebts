package mydebts.android.app.feature.events;

import dagger.Subcomponent;
import mydebts.android.app.di.SingleIn;
import mydebts.android.app.di.SubcomponentBuilder;

@SingleIn(EventsFragment.class)
@Subcomponent
public interface EventsSubcomponent {

    void inject(EventsFragment fragment);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder {

        EventsSubcomponent build();
    }
}

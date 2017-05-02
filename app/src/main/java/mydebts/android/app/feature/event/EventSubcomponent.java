package mydebts.android.app.feature.event;

import dagger.Subcomponent;
import mydebts.android.app.di.SingleIn;
import mydebts.android.app.di.SubcomponentBuilder;

@SingleIn(EventFragment.class)
@Subcomponent
public interface EventSubcomponent {

    void inject(EventFragment fragment);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder {

        EventSubcomponent build();
    }
}

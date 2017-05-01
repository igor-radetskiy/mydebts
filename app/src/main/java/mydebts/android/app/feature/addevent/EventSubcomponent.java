package mydebts.android.app.feature.addevent;

import dagger.Subcomponent;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.di.ActivitySubcomponent;

@ActivityScope
@Subcomponent
public interface EventSubcomponent {

    void inject(EventActivity activity);

    void inject(EventFragment activity);

    @Subcomponent.Builder
    interface Builder extends ActivitySubcomponent.Builder {

        EventSubcomponent build();
    }
}

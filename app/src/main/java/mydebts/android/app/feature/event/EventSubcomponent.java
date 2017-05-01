package mydebts.android.app.feature.event;

import dagger.Subcomponent;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.di.SubcomponentBuilder;

@ActivityScope
@Subcomponent
public interface EventSubcomponent {

    void inject(EventFragment fragment);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder {

        EventSubcomponent build();
    }
}

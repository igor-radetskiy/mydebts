package mydebts.android.app;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mydebts.android.app.di.ActivityKey;
import mydebts.android.app.di.ActivitySubcomponent;
import mydebts.android.app.feature.events.EventsActivity;
import mydebts.android.app.feature.events.EventsSubcomponent;

@Module(subcomponents = {
        EventsSubcomponent.class})
interface MyDebtsApplicationBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(EventsActivity.class)
    ActivitySubcomponent.Builder eventsSubcomponentBuilder(EventsSubcomponent.Builder impl);
}

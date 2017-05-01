package mydebts.android.app;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mydebts.android.app.di.ActivityKey;
import mydebts.android.app.di.ActivitySubcomponent;
import mydebts.android.app.feature.addevent.EventActivity;
import mydebts.android.app.feature.addevent.EventSubcomponent;
import mydebts.android.app.feature.events.EventsActivity;
import mydebts.android.app.feature.events.EventsSubcomponent;

@Module(subcomponents = {
        EventsSubcomponent.class,
        EventSubcomponent.class})
interface MyDebtsApplicationBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(EventsActivity.class)
    ActivitySubcomponent.Builder eventsSubcomponentBuilder(EventsSubcomponent.Builder impl);

    @Binds
    @IntoMap
    @ActivityKey(EventActivity.class)
    ActivitySubcomponent.Builder eventSubcomponentBuilder(EventSubcomponent.Builder impl);
}

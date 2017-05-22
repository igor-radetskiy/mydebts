package mydebts.android.app

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import mydebts.android.app.di.SubcomponentBuilder
import mydebts.android.app.di.SubcomponentBuilderKey
import mydebts.android.app.feature.event.EventFragment
import mydebts.android.app.feature.event.EventSubcomponent
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.events.EventsSubcomponent

@Module(subcomponents = arrayOf(EventsSubcomponent::class, EventSubcomponent::class))
internal interface MyDebtsApplicationBindingModule {
    @Binds
    @IntoMap
    @SubcomponentBuilderKey(EventsFragment::class)
    fun eventsSubcomponentBuilder(impl: EventsSubcomponent.Builder): SubcomponentBuilder

    @Binds
    @IntoMap
    @SubcomponentBuilderKey(EventFragment::class)
    fun eventSubcomponentBuilder(impl: EventSubcomponent.Builder): SubcomponentBuilder
}

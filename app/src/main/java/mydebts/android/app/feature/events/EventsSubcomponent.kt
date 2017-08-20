package mydebts.android.app.feature.events

import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(EventsFragment::class)
@Subcomponent(modules = arrayOf(EventsModule::class))
interface EventsSubcomponent {

    fun inject(fragment: EventsFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance fun fragent(fragment: EventsFragment): Builder

        fun build(): EventsSubcomponent
    }
}

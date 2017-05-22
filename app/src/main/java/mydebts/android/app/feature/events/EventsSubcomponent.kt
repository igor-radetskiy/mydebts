package mydebts.android.app.feature.events

import dagger.Subcomponent
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(EventsFragment::class)
@Subcomponent
interface EventsSubcomponent {

    fun inject(fragment: EventsFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        fun build(): EventsSubcomponent
    }
}

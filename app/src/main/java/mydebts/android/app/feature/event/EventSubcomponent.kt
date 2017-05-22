package mydebts.android.app.feature.event

import dagger.Subcomponent
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(EventFragment::class)
@Subcomponent
interface EventSubcomponent {

    fun inject(fragment: EventFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        fun build(): EventSubcomponent
    }
}

package mydebts.android.app.feature.event

import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.data.model.Event
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(EventFragment::class)
@Subcomponent(modules = arrayOf(EventModule::class))
interface EventSubcomponent {

    fun inject(fragment: EventFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance fun event(event: Event?): Builder

        @BindsInstance fun fragment(fragment: EventFragment): Builder

        fun build(): EventSubcomponent
    }
}

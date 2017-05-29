package mydebts.android.app.feature.persons

import dagger.Subcomponent
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(PersonsFragment::class)
@Subcomponent
interface PersonsSubcomponent {

    fun inject(fragment: PersonsFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        fun build(): PersonsSubcomponent
    }
}

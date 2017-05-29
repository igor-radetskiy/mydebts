package mydebts.android.app.feature.person

import dagger.Subcomponent
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(PersonFragment::class)
@Subcomponent
interface PersonSubcomonent {

    fun inject(fragment: PersonFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        fun build(): PersonSubcomonent
    }
}
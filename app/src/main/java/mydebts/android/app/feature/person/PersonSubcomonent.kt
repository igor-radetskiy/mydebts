package mydebts.android.app.feature.person

import android.app.Activity
import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(PersonActivity::class)
@Subcomponent(modules = arrayOf(PersonModule::class))
interface PersonSubcomonent {

    fun inject(activity: PersonActivity)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance fun activity(activity: Activity): Builder
        @BindsInstance fun person(person: Person): Builder

        fun build(): PersonSubcomonent
    }
}
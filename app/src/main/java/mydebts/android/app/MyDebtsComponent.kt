package mydebts.android.app

import android.content.Context

import dagger.BindsInstance
import dagger.Component
import mydebts.android.app.data.DataModule
import mydebts.android.app.res.ResourcesModule

import javax.inject.Singleton
import dagger.android.AndroidInjectionModule

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        DataModule::class,
        ResourcesModule::class,
        MyDebtsBindingModule::class))
internal interface MyDebtsComponent {

    fun inject(application: MyDebtsApplication)

    @Component.Builder
    interface Builder {

        fun build(): MyDebtsComponent

        @BindsInstance fun applicationContext(context: Context): Builder
    }
}

package mydebts.android.app

import android.content.Context

import dagger.BindsInstance
import dagger.Component
import mydebts.android.app.data.DataModule

import javax.inject.Singleton
import dagger.android.AndroidInjectionModule
import mydebts.android.app.feature.participant.ParticipantUiModule

@Singleton
@Component(modules = [AndroidInjectionModule::class, DataModule::class,
    ParticipantUiModule::class, MyDebtsBindingModule::class])
internal interface MyDebtsComponent {

    fun inject(application: MyDebtsApplication)

    @Component.Builder
    interface Builder {

        fun build(): MyDebtsComponent

        @BindsInstance fun applicationContext(context: Context): Builder
    }
}

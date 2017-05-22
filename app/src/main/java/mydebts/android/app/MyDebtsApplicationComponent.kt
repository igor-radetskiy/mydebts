package mydebts.android.app

import javax.inject.Singleton

import dagger.Component
import mydebts.android.app.data.DataModule

@Singleton
@Component(modules = arrayOf(MyDebtsApplicationModule::class, DataModule::class, MyDebtsApplicationBindingModule::class))
internal interface MyDebtsApplicationComponent {
    fun inject(application: MyDebtsApplication)
}

package mydebts.android.app

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

import javax.inject.Inject

class MyDebtsApplication : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerMyDebtsComponent.builder()
                .applicationContext(applicationContext)
                .build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}

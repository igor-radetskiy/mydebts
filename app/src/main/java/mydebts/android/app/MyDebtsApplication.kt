package mydebts.android.app

import android.app.Application

import javax.inject.Inject

import mydebts.android.app.di.SubcomponentBuilder

class MyDebtsApplication : Application() {

    @Inject lateinit var subcomponentBuilders: Map<Class<*>, @JvmSuppressWildcards SubcomponentBuilder>

    override fun onCreate() {
        super.onCreate()

        DaggerMyDebtsComponent.builder()
                .applicationContext(applicationContext)
                .build().inject(this)
    }

    fun subcomponentBuilder(keyClass: Class<*>): SubcomponentBuilder {
        return subcomponentBuilders[keyClass]!!
    }
}

package mydebts.android.app

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
internal class MyDebtsApplicationModule(private val context: Context) {

    @Provides
    fun provideApplicationContext(): Context {
        return context
    }
}

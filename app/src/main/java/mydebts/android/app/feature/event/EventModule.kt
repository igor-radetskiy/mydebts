package mydebts.android.app.feature.event

import dagger.Module
import dagger.Provides

@Module
class EventModule {
    @Provides fun provideScreen(fragment: EventFragment): EventScreen = fragment
}
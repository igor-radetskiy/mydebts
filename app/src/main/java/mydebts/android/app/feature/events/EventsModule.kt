package mydebts.android.app.feature.events

import dagger.Module
import dagger.Provides

@Module
class EventsModule {

    @Provides fun provideEventsScreen(fragment: EventsFragment): EventsScreen = fragment
}
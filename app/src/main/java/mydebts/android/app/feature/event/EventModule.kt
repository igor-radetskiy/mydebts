package mydebts.android.app.feature.event

import dagger.Module
import dagger.Provides
import mydebts.android.app.data.model.Event

@Module
class EventModule {

    @Provides fun provideEvent(fragment: EventFragment): Event?
            = fragment.arguments?.getParcelable(EventFragment.ARG_EVENT)

    @Provides fun provideScreen(fragment: EventFragment): EventScreen = fragment
}
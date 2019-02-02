package mydebts.android.app.ui.events

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mydebts.android.app.feature.events.EventsViewModel

@Module
class EventsModule {

    @Provides fun provideViewModel(activity: EventsActivity, factory: EventsViewModel.Factory):
            EventsViewModel = ViewModelProviders.of(activity, factory)[EventsViewModel::class.java]
}
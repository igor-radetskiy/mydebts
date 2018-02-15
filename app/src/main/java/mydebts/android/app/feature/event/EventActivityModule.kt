package mydebts.android.app.feature.event

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mydebts.android.app.data.model.Event

@Module
class EventActivityModule {

    @Provides fun provideEvent(activity: EventActivity): Event? =
            activity.intent?.getParcelableExtra(EventActivity.EXTRA_EVENT)

    @Provides fun provideViewModel(activity: EventActivity, factory: EventViewModel.Factory): EventViewModel =
            ViewModelProviders.of(activity, factory)[EventViewModel::class.java]
}
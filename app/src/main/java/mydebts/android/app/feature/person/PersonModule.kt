package mydebts.android.app.feature.person

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class PersonModule {

    @Provides fun provideViewModel(activity: PersonActivity, factory: PersonViewModel.Factory) : PersonViewModel
            = ViewModelProviders.of(activity, factory)[PersonViewModel::class.java]
}
package mydebts.android.app.feature.persons

import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class PersonsModule {

    @Provides fun provideViewModel(activity: PersonsActivity, factory: PersonsViewModel.Factory): PersonsViewModel
            = ViewModelProviders.of(activity, factory)[PersonsViewModel::class.java]
}
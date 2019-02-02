package mydebts.android.app.feature.people

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class PeopleModule {

    @Provides fun provideViewModel(activity: PeopleActivity, factory: PeopleViewModel.Factory): PeopleViewModel
            = ViewModelProviders.of(activity, factory)[PeopleViewModel::class.java]
}
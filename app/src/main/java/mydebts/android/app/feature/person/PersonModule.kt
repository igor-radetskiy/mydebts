package mydebts.android.app.feature.person

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mydebts.android.app.data.model.Person

@Module
class PersonModule {

    @Provides fun providePerson(activity: PersonActivity): Person
            = activity.intent.getParcelableExtra(PersonActivity.EXTRA_PERSON)

    @Provides fun provideViewModel(activity: PersonActivity, factory: PersonViewModel.Factory): PersonViewModel
            = ViewModelProviders.of(activity, factory)[PersonViewModel::class.java]
}
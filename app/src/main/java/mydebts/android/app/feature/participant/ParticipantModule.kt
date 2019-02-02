package mydebts.android.app.feature.participant

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mydebts.android.app.data.model.Participant

@Module
class ParticipantModule {

    @Provides fun provideParticipant(fragment: ParticipantDialogFragment): Participant?
            = fragment.arguments?.getParcelable(ParticipantDialogFragment.ARG_PARTICIPANT)

    @Provides fun provideViewModel(fragment: ParticipantDialogFragment,
                                   factory: ParticipantViewModel.Factory): ParticipantViewModel =
            ViewModelProviders.of(fragment, factory)[ParticipantViewModel::class.java]
}
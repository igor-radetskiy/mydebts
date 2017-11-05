package mydebts.android.app.feature.participant

import dagger.Module
import dagger.Provides
import mydebts.android.app.data.model.Participant

@Module
class ParticipantModule {

    @Provides fun provideParticipant(fragment: ParticipantDialogFragment): Participant?
            = fragment.arguments?.getParcelable(ParticipantDialogFragment.ARG_PARTICIPANT)

    @Provides fun provideParticipantScreen(fragment: ParticipantDialogFragment): ParticipantScreen = fragment
}
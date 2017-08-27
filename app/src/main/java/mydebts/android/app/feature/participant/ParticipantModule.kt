package mydebts.android.app.feature.participant

import dagger.Module
import dagger.Provides

@Module
class ParticipantModule {
    @Provides fun provideParticipantScreen(fragment: ParticipantDialogFragment): ParticipantScreen = fragment
}
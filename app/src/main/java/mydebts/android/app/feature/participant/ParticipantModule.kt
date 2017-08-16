package mydebts.android.app.feature.participant

import dagger.Module
import dagger.Provides
import mydebts.android.app.di.SingleIn

@Module
class ParticipantModule {
    @Provides fun provideParticipantScreen(activity: ParticipantActivity): ParticipantScreen = activity
}
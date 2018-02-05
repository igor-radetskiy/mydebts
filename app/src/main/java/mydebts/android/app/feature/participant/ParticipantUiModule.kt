package mydebts.android.app.feature.participant

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.ParticipantUi
import javax.inject.Singleton

@Module
class ParticipantUiModule {

    @Provides @ParticipantUi @Singleton internal fun provideParticipantUiSubject(): PublishSubject<Participant>
            = PublishSubject.create<Participant>()

    @Provides @ParticipantUi @Singleton internal fun provideParticipantUiObservable(
            @ParticipantUi publishSubject: PublishSubject<Participant>): Observable<Participant> = publishSubject
}
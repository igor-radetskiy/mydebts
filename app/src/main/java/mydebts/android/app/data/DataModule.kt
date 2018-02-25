package mydebts.android.app.data

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.db.DbModule
import mydebts.android.app.data.internal.EventsSourceImpl
import mydebts.android.app.data.internal.ParticipantsSourceImpl
import mydebts.android.app.data.internal.PersonsSourceImpl
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Person

@Module(includes = [DbModule::class])
class DataModule {

    @Provides
    @Singleton
    internal fun provideEventsSource(impl: EventsSourceImpl): EventsSource = impl

    @Provides
    @Singleton
    internal fun providePersonsSource(impl: PersonsSourceImpl): PersonsSource = impl

    @Provides
    @Singleton
    internal fun provideParticipantsSource(impl: ParticipantsSourceImpl): ParticipantsSource = impl

    @Provides
    @Insert
    @Singleton
    internal fun provideInsertEventSubject(): PublishSubject<Event> = PublishSubject.create<Event>()

    @Provides
    @Insert
    @Singleton
    internal fun provideInsertEventObservable(
            @Insert subject: PublishSubject<Event>): Observable<Event> = subject

    @Provides
    @Update
    @Singleton
    internal fun provideUpdateEventSubject(): PublishSubject<Event> = PublishSubject.create<Event>()

    @Provides
    @Update
    @Singleton
    internal fun provideUpdateEventObservable(
            @Update subject: PublishSubject<Event>): Observable<Event> = subject

    @Provides
    @Delete
    @Singleton
    internal fun provideDeleteEventSubject(): PublishSubject<Event> = PublishSubject.create<Event>()

    @Provides
    @Delete
    @Singleton
    internal fun provideDeleteEventObservable(
            @Delete subject: PublishSubject<Event>): Observable<Event> = subject

    @Provides
    @Insert
    @Singleton
    internal fun provideInsertPersonSubject(): PublishSubject<Person> = PublishSubject.create<Person>()

    @Provides
    @Update
    @Singleton
    internal fun provideUpdatePersonSubject(): PublishSubject<Person> = PublishSubject.create<Person>()

    @Provides
    @Delete
    @Singleton
    internal fun provideDeletePersonSubject(): PublishSubject<Person> = PublishSubject.create<Person>()
}

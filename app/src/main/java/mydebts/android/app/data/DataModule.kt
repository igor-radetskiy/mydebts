package mydebts.android.app.data

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.db.DbModule
import mydebts.android.app.data.internal.EventsSourceImpl
import mydebts.android.app.data.internal.ParticipantsSourceImpl
import mydebts.android.app.data.internal.PersonsSourceImpl
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.DeleteSubject
import mydebts.android.app.di.InsertSubject
import mydebts.android.app.di.UpdateSubject

@Module(includes = arrayOf(DbModule::class))
class DataModule {
    @Provides
    @Singleton
    internal fun provideEventsSource(impl: EventsSourceImpl): EventsSource {
        return impl
    }

    @Provides
    @Singleton
    internal fun providePersonsSource(impl: PersonsSourceImpl): PersonsSource {
        return impl
    }

    @Provides
    @Singleton
    internal fun provideParticipantsSource(impl: ParticipantsSourceImpl): ParticipantsSource {
        return impl
    }

    @Provides
    @InsertSubject
    @Singleton
    internal fun provideInsertedEventSubject(): PublishSubject<Event> {
        return PublishSubject.create<Event>()
    }

    @Provides
    @UpdateSubject
    @Singleton
    internal fun provideUpdatedEventSubject(): PublishSubject<Event> {
        return PublishSubject.create<Event>()
    }

    @Provides
    @DeleteSubject
    @Singleton
    internal fun provideDeletedEventSubject(): PublishSubject<Event> {
        return PublishSubject.create<Event>()
    }

    @Provides
    @InsertSubject
    @Singleton
    internal fun provideInsertedPersonSubject(): PublishSubject<Person> {
        return PublishSubject.create<Person>()
    }

    @Provides
    @UpdateSubject
    @Singleton
    internal fun provideUpdatedPersonSubject(): PublishSubject<Person> {
        return PublishSubject.create<Person>()
    }

    @Provides
    @DeleteSubject
    @Singleton
    internal fun provideDeletedPersonSubject(): PublishSubject<Person> {
        return PublishSubject.create<Person>()
    }
}

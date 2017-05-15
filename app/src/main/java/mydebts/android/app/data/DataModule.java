package mydebts.android.app.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;
import mydebts.android.app.data.db.DbModule;
import mydebts.android.app.data.internal.EventsSourceImpl;
import mydebts.android.app.data.internal.ParticipantsSourceImpl;
import mydebts.android.app.data.internal.PersonsSourceImpl;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.data.model.Person;
import mydebts.android.app.di.DeleteSubject;
import mydebts.android.app.di.InsertSubject;
import mydebts.android.app.di.UpdateSubject;

@Module(includes = DbModule.class)
public class DataModule {
    @Provides
    @Singleton
    EventsSource provideEventsSource(EventsSourceImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    PersonsSource providePersonsSource(PersonsSourceImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    ParticipantsSource provideParticipantsSource(ParticipantsSourceImpl impl) {
        return impl;
    }

    @Provides
    @InsertSubject
    @Singleton
    PublishSubject<Event> provideInsertedEventSubject() {
        return PublishSubject.create();
    }

    @Provides
    @UpdateSubject
    @Singleton
    PublishSubject<Event> provideUpdatedEventSubject() {
        return PublishSubject.create();
    }

    @Provides
    @DeleteSubject
    @Singleton
    PublishSubject<Event> provideDeletedEventSubject() {
        return PublishSubject.create();
    }

    @Provides
    @InsertSubject
    @Singleton
    PublishSubject<Person> provideInsertedPersonSubject() {
        return PublishSubject.create();
    }

    @Provides
    @UpdateSubject
    @Singleton
    PublishSubject<Person> provideUpdatedPersonSubject() {
        return PublishSubject.create();
    }

    @Provides
    @DeleteSubject
    @Singleton
    PublishSubject<Person> provideDeletedPersonSubject() {
        return PublishSubject.create();
    }
}

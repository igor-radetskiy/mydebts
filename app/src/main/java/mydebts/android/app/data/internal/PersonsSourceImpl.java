package mydebts.android.app.data.internal;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import mydebts.android.app.data.PersonsSource;
import mydebts.android.app.data.db.PersonsTableDao;
import mydebts.android.app.data.db.PersonsTable;
import mydebts.android.app.data.model.Person;
import mydebts.android.app.di.DeleteSubject;
import mydebts.android.app.di.InsertSubject;
import mydebts.android.app.di.UpdateSubject;

public class PersonsSourceImpl implements PersonsSource {
    private static final PersonToPersonsTable TO_DB_MAPPER = new PersonToPersonsTable();
    private static final PersonsTableToPerson FROM_DB_MAPPER = new PersonsTableToPerson();
    private static final ListToList<PersonsTable, Person> FROM_DB_LIST_MAPPER = new ListToList<>(FROM_DB_MAPPER);

    private final PersonsTableDao dao;
    private final PublishSubject<Person> insertedPersonSubject;
    private final PublishSubject<Person> updatedPersonSubject;
    private final PublishSubject<Person> deletedPersonSubject;

    @Inject
    PersonsSourceImpl(PersonsTableDao dao, @InsertSubject PublishSubject<Person> insertedPersonSubject,
                      @UpdateSubject PublishSubject<Person> updatedPersonSubject,
                      @DeleteSubject PublishSubject<Person> deletedPersonSubject) {
        this.dao = dao;
        this.insertedPersonSubject = insertedPersonSubject;
        this.updatedPersonSubject = updatedPersonSubject;
        this.deletedPersonSubject = deletedPersonSubject;
    }

    @Override
    public Single<List<Person>> getAll() {
        return Single.fromCallable(dao::loadAll)
                .map(FROM_DB_LIST_MAPPER);
    }

    @Override
    public Single<Person> get(@NonNull Long id) {
        return Single.fromCallable(() -> dao.load(id))
                .map(FROM_DB_MAPPER);
    }

    @Override
    public Single<Person> insert(@NonNull Person person) {
        return Single.just(person)
                .map(TO_DB_MAPPER)
                .flatMap(this::insertToDb)
                .map(id -> Person.builder()
                        .id(id)
                        .build())
                .doOnSuccess(insertedPersonSubject::onNext);
    }

    @Override
    public Single<Person> update(@NonNull Person person) {
        return Single.just(person)
                .map(TO_DB_MAPPER)
                .flatMap(this::updateInDb)
                .map(obj -> person)
                .doOnSuccess(updatedPersonSubject::onNext);
    }

    @Override
    public Single<Person> delete(@NonNull Person person) {
        return Single.just(person)
                .map(TO_DB_MAPPER)
                .flatMap(this::deleteFromDb)
                .map(obj -> person)
                .doOnSuccess(deletedPersonSubject::onNext);
    }

    @Override
    public Observable<Person> inserted() {
        return insertedPersonSubject;
    }

    @Override
    public Observable<Person> updated() {
        return updatedPersonSubject;
    }

    @Override
    public Observable<Person> deleted() {
        return deletedPersonSubject;
    }

    private Single<Long> insertToDb(PersonsTable personsTable) {
        return Single.fromCallable(() -> dao.insert(personsTable));
    }

    private Single<Void> updateInDb(PersonsTable personsTable) {
        return Single.fromCallable(() -> {
            dao.update(personsTable);
            return null;
        });
    }

    private Single<Void> deleteFromDb(PersonsTable personsTable) {
        return Single.fromCallable(() -> {
            dao.delete(personsTable);
            return null;
        });
    }
}

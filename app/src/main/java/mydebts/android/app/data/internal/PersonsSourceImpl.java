package mydebts.android.app.data.internal;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import mydebts.android.app.data.PersonsSource;
import mydebts.android.app.data.db.PersonTable;
import mydebts.android.app.data.model.Person;
import mydebts.android.app.di.DeleteSubject;
import mydebts.android.app.di.InsertSubject;
import mydebts.android.app.di.UpdateSubject;

public class PersonsSourceImpl implements PersonsSource {
    private static final CursorToPerson CURSOR_TO_PERSON = new CursorToPerson();
    private static final ClosableCursorToEntity<Person> CLOSABLE_CURSOR_TO_PERSON
            = new ClosableCursorToEntity<>(CURSOR_TO_PERSON);
    private static final ClosableCursorToList<Person> CLOSABLE_CURSOR_TO_PERSONS =
            new ClosableCursorToList<>(CURSOR_TO_PERSON);

    private final PersonTable personTable;
    private final PublishSubject<Person> insertedPersonSubject;
    private final PublishSubject<Person> updatedPersonSubject;
    private final PublishSubject<Person> deletedPersonSubject;

    @Inject
    PersonsSourceImpl(PersonTable personTable, @InsertSubject PublishSubject<Person> insertedPersonSubject,
                      @UpdateSubject PublishSubject<Person> updatedPersonSubject,
                      @DeleteSubject PublishSubject<Person> deletedPersonSubject) {
        this.personTable = personTable;
        this.insertedPersonSubject = insertedPersonSubject;
        this.updatedPersonSubject = updatedPersonSubject;
        this.deletedPersonSubject = deletedPersonSubject;
    }

    @Override
    public Single<List<Person>> getAll() {
        return Single.fromCallable(personTable::queryAll)
                .map(CLOSABLE_CURSOR_TO_PERSONS);
    }

    @Override
    public Single<Person> get(@NonNull Long id) {
        return Single.fromCallable(() -> personTable.queryById(id))
                .map(CLOSABLE_CURSOR_TO_PERSON);
    }

    @Override
    public Single<Person> insert(@NonNull Person person) {
        return Single.fromCallable(() -> personTable.insert(person))
                .map(id -> Person.builder()
                        .id(id)
                        .build())
                .doOnSuccess(insertedPersonSubject::onNext);
    }

    @Override
    public Single<Person> update(@NonNull Person person) {
        return Single.fromCallable(() -> personTable.update(person))
                .map(affectedRows -> person)
                .doOnSuccess(updatedPersonSubject::onNext);
    }

    @Override
    public Single<Person> delete(@NonNull Person person) {
        return Single.fromCallable(() -> personTable.delete(person))
                .map(affectedRows -> person)
                .doOnSuccess(deletedPersonSubject::onNext);
    }
}

package mydebts.android.app.data.internal

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.db.PersonTable
import mydebts.android.app.data.model.Person
import mydebts.android.app.di.DeleteSubject
import mydebts.android.app.di.InsertSubject
import mydebts.android.app.di.UpdateSubject

class PersonsSourceImpl @Inject
internal constructor(private val personTable: PersonTable, @param:InsertSubject private val insertedPersonSubject: PublishSubject<Person>,
                     @param:UpdateSubject private val updatedPersonSubject: PublishSubject<Person>,
                     @param:DeleteSubject private val deletedPersonSubject: PublishSubject<Person>) : PersonsSource {

    override fun getAll(): Single<List<Person>> {
        return Single.fromCallable { personTable.queryAll() }
                .map(CLOSABLE_CURSOR_TO_PERSONS)
    }

    override fun get(id: Long): Single<Person> {
        return Single.fromCallable { personTable.queryById(id) }
                .map(CLOSABLE_CURSOR_TO_PERSON)
    }

    override fun insert(person: Person): Single<Person> {
        return Single.fromCallable { personTable.insert(person) }
                .map { id -> Person(id, person.name) }
                .doOnSuccess { insertedPersonSubject.onNext(it) }
    }

    override fun update(person: Person): Single<Person> {
        return Single.fromCallable { personTable.update(person) }
                .map { _ -> person }
                .doOnSuccess { updatedPersonSubject.onNext(it) }
    }

    override fun delete(person: Person): Single<Person> {
        return Single.fromCallable { personTable.delete(person) }
                .map { _ -> person }
                .doOnSuccess { deletedPersonSubject.onNext(it) }
    }

    companion object {
        private val CURSOR_TO_PERSON = CursorToPerson()
        private val CLOSABLE_CURSOR_TO_PERSON = ClosableCursorToEntity(CURSOR_TO_PERSON)
        private val CLOSABLE_CURSOR_TO_PERSONS = ClosableCursorToList(CURSOR_TO_PERSON)
    }
}

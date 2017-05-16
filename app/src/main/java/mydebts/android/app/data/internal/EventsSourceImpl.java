package mydebts.android.app.data.internal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import mydebts.android.app.data.EventsSource;
import mydebts.android.app.data.db.CursorToEvent;
import mydebts.android.app.data.db.CursorToList;
import mydebts.android.app.data.db.EventContract;
import mydebts.android.app.data.db.EventsTableDao;
import mydebts.android.app.data.db.EventsTable;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.di.DeleteSubject;
import mydebts.android.app.di.InsertSubject;
import mydebts.android.app.di.UpdateSubject;

public class EventsSourceImpl implements EventsSource {
    private static final EventToEventsTable TO_DB_MAPPER = new EventToEventsTable();
    private static final EventsTableToEvent FROM_DB_MAPPER = new EventsTableToEvent();
    private static final ListToList<EventsTable,Event> FROM_DB_LIST_MAPPER = new ListToList<>(FROM_DB_MAPPER);

    private static final CursorToEvent FROMDB_MAPPER = new CursorToEvent();
    private static final CursorToList<Event> FROMDB_LIST_MAPPER = new CursorToList<>(FROMDB_MAPPER);

    private final EventsTableDao dao;
    private final SQLiteDatabase db;
    private final PublishSubject<Event> insertedEventSubject;
    private final PublishSubject<Event> updatedEventSubject;
    private final PublishSubject<Event> deletedEventSubject;

    @Inject
    EventsSourceImpl(EventsTableDao dao, SQLiteDatabase db,
                            @InsertSubject PublishSubject<Event> insertedEventSubject,
                            @UpdateSubject PublishSubject<Event> updatedEventSubject,
                            @DeleteSubject PublishSubject<Event> deletedEventSubject) {
        this.dao = dao;
        this.db = db;
        this.insertedEventSubject = insertedEventSubject;
        this.updatedEventSubject = updatedEventSubject;
        this.deletedEventSubject = deletedEventSubject;
    }

    @Override
    public Single<List<Event>> getAll() {
        return Single.fromCallable(this::queryAll)
                .map(FROMDB_LIST_MAPPER);
    }

    @Override
    public Single<Event> get(@NonNull Long id) {
        return Single.fromCallable(() -> dao.load(id))
                .map(FROM_DB_MAPPER);
    }

    @Override
    public Single<Event> insert(@NonNull Event event) {
        return Single.just(event)
                .map(TO_DB_MAPPER)
                .flatMap(this::insertToDb)
                .map(id -> Event.builder(event)
                        .id(id)
                        .build())
                .doOnSuccess(insertedEventSubject::onNext);
    }

    @Override
    public Single<Event> update(@NonNull Event event) {
        return Single.just(event)
                .map(TO_DB_MAPPER)
                .flatMap(this::updateInDb)
                .map(obj -> event)
                .doOnSuccess(updatedEventSubject::onNext);
    }

    @Override
    public Single<Event> delete(@NonNull Event event) {
        return Single.just(event)
                .map(TO_DB_MAPPER)
                .flatMap(this::deleteFromDb)
                .map(obj -> event)
                .doOnSuccess(deletedEventSubject::onNext);
    }

    private Single<Long> insertToDb(EventsTable eventsTable) {
        return Single.fromCallable(() -> dao.insert(eventsTable));
    }

    private Single<Boolean> updateInDb(EventsTable eventsTable) {
        return Single.fromCallable(() -> {
            dao.update(eventsTable);
            return true;
        });
    }

    private Single<Boolean> deleteFromDb(EventsTable eventsTable) {
        return Single.fromCallable(() -> {
            dao.delete(eventsTable);
            return true;
        });
    }

    private Cursor queryAll() {
        return db.query(EventContract.TABLE_NAME, null, null, null, null, null, null);
    }
}

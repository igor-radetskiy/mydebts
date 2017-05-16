package mydebts.android.app.data.internal;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import mydebts.android.app.data.EventsSource;
import mydebts.android.app.data.db.EventTable;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.di.DeleteSubject;
import mydebts.android.app.di.InsertSubject;
import mydebts.android.app.di.UpdateSubject;

public class EventsSourceImpl implements EventsSource {
    private static final CursorToEvent ITEM_MAPPER = new CursorToEvent();
    private static final ClosableCursorToEntity<Event> FROMD_MAPPER = new ClosableCursorToEntity<>(ITEM_MAPPER);
    private static final ClosableCursorToList<Event> FROMDB_LIST_MAPPER = new ClosableCursorToList<>(ITEM_MAPPER);

    private final EventTable eventTable;
    private final PublishSubject<Event> insertedEventSubject;
    private final PublishSubject<Event> updatedEventSubject;
    private final PublishSubject<Event> deletedEventSubject;

    @Inject
    EventsSourceImpl(EventTable eventTable, @InsertSubject PublishSubject<Event> insertedEventSubject,
                            @UpdateSubject PublishSubject<Event> updatedEventSubject,
                            @DeleteSubject PublishSubject<Event> deletedEventSubject) {
        this.eventTable = eventTable;
        this.insertedEventSubject = insertedEventSubject;
        this.updatedEventSubject = updatedEventSubject;
        this.deletedEventSubject = deletedEventSubject;
    }

    @Override
    public Single<List<Event>> getAll() {
        return Single.fromCallable(eventTable::queryAll)
                .map(FROMDB_LIST_MAPPER);
    }

    @Override
    public Single<Event> get(@NonNull Long id) {
        return Single.fromCallable(() -> eventTable.queryById(id))
                .map(FROMD_MAPPER);
    }

    @Override
    public Single<Event> insert(@NonNull Event event) {
        return Single.fromCallable(() -> eventTable.insert(event))
                .map(id -> Event.builder(event)
                        .id(id)
                        .build())
                .doOnSuccess(insertedEventSubject::onNext);
    }

    @Override
    public Single<Event> update(@NonNull Event event) {
        return Single.fromCallable(() -> eventTable.update(event))
                .map(affectedRows -> event)
                .doOnSuccess(updatedEventSubject::onNext);
    }

    @Override
    public Single<Event> delete(@NonNull Event event) {
        return Single.fromCallable(() -> eventTable.delete(event))
                .map(affectedRows -> event)
                .doOnSuccess(deletedEventSubject::onNext);
    }
}

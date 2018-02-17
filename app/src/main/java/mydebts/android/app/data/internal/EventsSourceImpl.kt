package mydebts.android.app.data.internal

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.db.EventTable
import mydebts.android.app.data.model.Event
import mydebts.android.app.data.DeleteSubject
import mydebts.android.app.data.InsertSubject
import mydebts.android.app.data.UpdateSubject

class EventsSourceImpl @Inject
internal constructor(private val eventTable: EventTable, @param:InsertSubject private val insertedEventSubject: PublishSubject<Event>,
                     @param:UpdateSubject private val updatedEventSubject: PublishSubject<Event>,
                     @param:DeleteSubject private val deletedEventSubject: PublishSubject<Event>) : EventsSource {

    override fun getAll(): Single<List<Event>> {
        return Single.fromCallable { eventTable.queryAll() }
                .map(FROMDB_LIST_MAPPER)
    }

    override fun get(id: Long): Single<Event> {
        return Single.fromCallable { eventTable.queryById(id) }
                .map(FROMD_MAPPER)
    }

    override fun insert(event: Event): Single<Event> {
        return Single.fromCallable { eventTable.insert(event) }
                .map { id -> Event(id, event.name, event.date) }
                .doOnSuccess { insertedEventSubject.onNext(it) }
    }

    override fun update(event: Event): Single<Event> {
        return Single.fromCallable { eventTable.update(event) }
                .map { _ -> event }
                .doOnSuccess { updatedEventSubject.onNext(it) }
    }

    override fun delete(event: Event): Single<Event> {
        return Single.fromCallable { eventTable.delete(event) }
                .map { _ -> event }
                .doOnSuccess { deletedEventSubject.onNext(it) }
    }

    override fun delete(events: List<Event>): Single<List<Event>> {
        return Single.fromCallable { eventTable.delete(events) }
                .map { _ -> events }
                .doOnSuccess { deletedEvents -> deletedEvents.forEach { deletedEventSubject.onNext(it) } }
    }

    companion object {
        private val ITEM_MAPPER = CursorToEvent()
        private val FROMD_MAPPER = ClosableCursorToEntity(ITEM_MAPPER)
        private val FROMDB_LIST_MAPPER = ClosableCursorToList(ITEM_MAPPER)
    }
}

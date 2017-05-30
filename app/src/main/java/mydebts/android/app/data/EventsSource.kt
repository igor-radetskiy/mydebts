package mydebts.android.app.data

import io.reactivex.Single
import mydebts.android.app.data.model.Event

interface EventsSource {

    fun getAll(): Single<List<Event>>

    fun get(id: Long): Single<Event>

    fun insert(event: Event): Single<Event>

    fun update(event: Event): Single<Event>

    fun delete(event: Event): Single<Event>

    fun delete(events: List<Event>): Single<List<Event>>
}

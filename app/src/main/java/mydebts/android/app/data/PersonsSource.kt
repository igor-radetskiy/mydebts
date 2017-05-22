package mydebts.android.app.data

import io.reactivex.Single
import mydebts.android.app.data.model.Person

interface PersonsSource {

    fun getAll(): Single<List<Person>>

    fun get(id: Long): Single<Person>

    fun insert(person: Person): Single<Person>

    fun update(person: Person): Single<Person>

    fun delete(person: Person): Single<Person>
}

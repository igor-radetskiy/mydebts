package mydebts.android.app.data

import io.reactivex.Single
import mydebts.android.app.data.model.Participant

interface ParticipantsSource {

    fun getByEventId(eventId: Long): Single<List<Participant>>

    fun getByPersonId(personId: Long): Single<List<Participant>>

    fun insert(participant: Participant): Single<Participant>

    fun update(participant: Participant): Single<Participant>

    fun delete(participant: Participant): Single<Participant>
}

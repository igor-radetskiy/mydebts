package mydebts.android.app.data.internal

import javax.inject.Inject

import io.reactivex.Single
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.db.ParticipantTable
import mydebts.android.app.data.model.Participant

class ParticipantsSourceImpl @Inject
internal constructor(private val participantTable: ParticipantTable) : ParticipantsSource {

    override fun getByEventId(eventId: Long): Single<List<Participant>> {
        return Single.fromCallable { participantTable.queryByEventId(eventId) }
                .map(CLOSABLE_CURSOR_TO_PARTICIPANTS)
    }

    override fun getByPersonId(personId: Long): Single<List<Participant>> {
        return Single.fromCallable { participantTable.queryByPersonId(personId) }
                .map(CLOSABLE_CURSOR_TO_PARTICIPANTS)
    }

    override fun insert(participant: Participant): Single<Participant> {
        return Single.fromCallable { participantTable.insert(participant) }
                .map { id -> Participant(id, participant.event, participant.person, participant.debt) }
    }

    override fun update(participant: Participant): Single<Participant> {
        return Single.fromCallable { participantTable.update(participant) }
                .map { _ -> participant }
    }

    override fun delete(participant: Participant): Single<Participant> {
        return Single.fromCallable { participantTable.delete(participant) }
                .map { _ -> participant }
    }

    companion object {
        private val CURSOR_TO_PARTICIPANT = CursorToParticipant()
        private val CLOSABLE_CURSOR_TO_PARTICIPANTS = ClosableCursorToList(CURSOR_TO_PARTICIPANT)
    }
}

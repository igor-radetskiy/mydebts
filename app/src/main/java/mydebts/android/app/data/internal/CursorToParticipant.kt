package mydebts.android.app.data.internal

import android.database.Cursor

import io.reactivex.functions.Function
import mydebts.android.app.data.db.ParticipantContract
import mydebts.android.app.data.model.Participant

internal class CursorToParticipant : Function<Cursor, Participant> {

    @Throws(Exception::class)
    override fun apply(cursor: Cursor): Participant {
        return Participant.builder()
                .id(cursor.getLong(cursor.getColumnIndex(ParticipantContract._ID)))
                .event(CURSOR_TO_EVENT.apply(cursor))
                .person(CURSOR_TO_PERSON.apply(cursor))
                .debt(cursor.getDouble(cursor.getColumnIndex(ParticipantContract.COLUMN_DEBT)))
                .build()
    }

    companion object {
        private val CURSOR_TO_EVENT = CursorToEvent()
        private val CURSOR_TO_PERSON = CursorToPerson()
    }
}

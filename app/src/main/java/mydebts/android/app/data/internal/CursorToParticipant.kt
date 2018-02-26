package mydebts.android.app.data.internal

import android.database.Cursor

import io.reactivex.functions.Function
import mydebts.android.app.data.db.ParticipantContract
import mydebts.android.app.data.model.Participant

internal class CursorToParticipant : Function<Cursor, Participant> {

    @Throws(Exception::class)
    override fun apply(cursor: Cursor): Participant {
        return Participant(cursor.getLong(cursor.getColumnIndex(ParticipantContract.COLUMN_ID)),
                CURSOR_TO_EVENT.apply(cursor),
                CURSOR_TO_PERSON.apply(cursor),
                cursor.getDouble(cursor.getColumnIndex(ParticipantContract.COLUMN_DEBT)))
    }

    companion object {
        private val CURSOR_TO_EVENT = CursorToEvent()
        private val CURSOR_TO_PERSON = CursorToPerson()
    }
}

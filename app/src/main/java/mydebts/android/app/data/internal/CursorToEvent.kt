package mydebts.android.app.data.internal

import android.database.Cursor

import java.util.Date

import io.reactivex.functions.Function
import mydebts.android.app.data.db.EventContract
import mydebts.android.app.data.db.ParticipantContract
import mydebts.android.app.data.model.Event

internal class CursorToEvent : Function<Cursor, Event> {
    @Throws(Exception::class)
    override fun apply(cursor: Cursor): Event {
        val idColumnIndex = if (cursor.getColumnIndex(ParticipantContract.COLUMN_EVENT_ID) >= 0)
            cursor.getColumnIndex(ParticipantContract.COLUMN_EVENT_ID)
        else
            cursor.getColumnIndex(EventContract.COLUMN_ID)

        return Event(cursor.getLong(idColumnIndex),
                cursor.getString(cursor.getColumnIndex(EventContract.COLUMN_NAME)),
                Date(cursor.getLong(cursor.getColumnIndex(EventContract.COLUMN_DATE))))
    }
}

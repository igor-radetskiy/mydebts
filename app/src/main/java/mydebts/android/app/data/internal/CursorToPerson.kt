package mydebts.android.app.data.internal

import android.database.Cursor

import io.reactivex.functions.Function
import mydebts.android.app.data.db.ParticipantContract
import mydebts.android.app.data.db.PersonContract
import mydebts.android.app.data.model.Person

internal class CursorToPerson : Function<Cursor, Person> {
    @Throws(Exception::class)
    override fun apply(cursor: Cursor): Person {
        val idColumnIndex = if (cursor.getColumnIndex(ParticipantContract.COLUMN_PERSON_ID) >= 0)
            cursor.getColumnIndex(ParticipantContract.COLUMN_PERSON_ID)
        else
            cursor.getColumnIndex(PersonContract._ID)

        return Person.builder()
                .id(cursor.getLong(idColumnIndex))
                .name(cursor.getString(cursor.getColumnIndex(PersonContract.COLUMN_NAME)))
                .build()
    }
}

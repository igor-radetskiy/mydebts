package mydebts.android.app.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import javax.inject.Inject

import mydebts.android.app.data.model.Participant

class ParticipantTable @Inject
constructor(private val db: SQLiteDatabase) {

    fun queryByEventId(eventId: Long): Cursor {
        val cursor = query(ParticipantContract.COLUMN_EVENT_ID + " = ?", arrayOf(eventId.toString()))

        Log.d(TAG, "Query participants by event id; eventId = " + eventId + "; number of rows = " + cursor.count)

        return cursor
    }

    fun queryByPersonId(personId: Long): Cursor {
        val cursor = query(ParticipantContract.COLUMN_PERSON_ID + " = ?", arrayOf(personId.toString()))

        Log.d(TAG, "Query participants by person id; personId = " + personId + "; number of rows = " + cursor.count)

        return cursor
    }


    fun insert(participant: Participant): Long {
        val contentValues = ContentValues()
        contentValues.put(ParticipantContract.COLUMN_EVENT_ID, participant.event!!.id)
        contentValues.put(ParticipantContract.COLUMN_PERSON_ID, participant.person!!.id)
        contentValues.put(ParticipantContract.COLUMN_DEBT, participant.debt)

        val id = db.insert(ParticipantContract.TABLE_NAME, null, contentValues)

        Log.d(TAG, "Insert participant = $participant; row id = $id")

        return id
    }

    fun update(participant: Participant): Int {
        val contentValues = ContentValues()
        contentValues.put(ParticipantContract.COLUMN_EVENT_ID, participant.event!!.id)
        contentValues.put(ParticipantContract.COLUMN_PERSON_ID, participant.person!!.id)
        contentValues.put(ParticipantContract.COLUMN_DEBT, participant.debt)

        val affectedRows = db.update(ParticipantContract.TABLE_NAME, contentValues,
                ParticipantContract._ID + " = ?", arrayOf(participant.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Update participant " + participant)
        } else {
            Log.e(TAG, "Cannot update participant " + participant)
        }

        return affectedRows
    }

    fun delete(participant: Participant): Int {
        val affectedRows = db.delete(ParticipantContract.TABLE_NAME,
                ParticipantContract._ID + " = ?", arrayOf(participant.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Delete participant " + participant)
        } else {
            Log.e(TAG, "Cannot delete participant " + participant)
        }

        return affectedRows
    }

    private fun query(selection: String, selectionArgs: Array<String>): Cursor {
        val joinTables = ParticipantContract.TABLE_NAME + " INNER JOIN " + EventContract.TABLE_NAME + " ON " +
                ParticipantContract.COLUMN_EVENT_ID + " = " + EventContract.TABLE_NAME + "." + EventContract._ID +
                " INNER JOIN " + PersonContract.TABLE_NAME + " ON " +
                ParticipantContract.COLUMN_PERSON_ID + " = " + PersonContract.TABLE_NAME + "." + PersonContract._ID

        return db.query(joinTables, null, selection, selectionArgs, null, null, null)
    }

    companion object {
        private val TAG = ParticipantTable::class.java.simpleName
    }
}

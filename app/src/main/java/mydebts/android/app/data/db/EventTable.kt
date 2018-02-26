package mydebts.android.app.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import javax.inject.Inject
import javax.inject.Singleton

import mydebts.android.app.data.model.Event

@Singleton
class EventTable @Inject
internal constructor(private val db: SQLiteDatabase) {

    fun queryAll(): Cursor {
        val cursor = db.query(EventContract.TABLE_NAME, null, null, null, null, null, null)

        Log.d(TAG, "Query all events; number of rows = " + cursor.count)

        return cursor
    }

    fun queryById(id: Long): Cursor {
        val cursor = db.query(EventContract.TABLE_NAME, null,
                EventContract.COLUMN_ID + " = ?", arrayOf(id.toString()), null, null, null)

        Log.d(TAG, "Query event by id = " + id + "; number of rows = " + cursor.count)

        return cursor
    }

    fun insert(event: Event): Long {
        val contentValues = ContentValues()
        contentValues.put(EventContract.COLUMN_NAME, event.name)
        contentValues.put(EventContract.COLUMN_DATE, event.date!!.time)

        val id = db.insert(EventContract.TABLE_NAME, null, contentValues)

        Log.d(TAG, "Insert event = $event; row id = $id")

        return id
    }

    fun update(event: Event): Int {
        val contentValues = ContentValues()
        contentValues.put(EventContract.COLUMN_NAME, event.name)
        contentValues.put(EventContract.COLUMN_DATE, event.date!!.time)

        val affectedRows = db.update(EventContract.TABLE_NAME, contentValues,
                EventContract.COLUMN_ID + " = ?", arrayOf(event.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Update event " + event)
        } else {
            Log.e(TAG, "Cannot update event " + event)
        }

        return affectedRows
    }

    fun delete(event: Event): Int {
        val affectedRows = db.delete(EventContract.TABLE_NAME,
                EventContract.COLUMN_ID + " = ?", arrayOf(event.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Delete event " + event)
        } else {
            Log.e(TAG, "Cannot delete event " + event)
        }

        return affectedRows
    }

    fun delete(events: List<Event>): Int {
        if (events.isEmpty()) {
            Log.d(TAG, "No events to be deleted")
            return 0
        }

        val whereClause = StringBuilder(EventContract.COLUMN_ID + " IN (")
        for (i in 1..events.size) {
            whereClause.append("?,")
        }
        if (whereClause[whereClause.lastIndex] == ',') {
            whereClause.deleteCharAt(whereClause.lastIndex)
        }
        whereClause.append(")")

        val affectedRows = db.delete(EventContract.TABLE_NAME, whereClause.toString(),
                Array(events.size, { position -> events[position].id.toString()}))

        Log.d(TAG, "Delete events " + events)

        return affectedRows
    }

    companion object {
        private const val TAG = "EventTable"
    }
}

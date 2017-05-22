package mydebts.android.app.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import javax.inject.Inject

import mydebts.android.app.data.model.Person

class PersonTable @Inject
constructor(private val db: SQLiteDatabase) {

    fun queryAll(): Cursor {
        val cursor = db.query(PersonContract.TABLE_NAME, null, null, null, null, null, null)

        Log.d(TAG, "Query all persons; number of rows = " + cursor.count)

        return cursor
    }

    fun queryById(id: Long): Cursor {
        val cursor = db.query(PersonContract.TABLE_NAME, null,
                PersonContract._ID + " = ?", arrayOf(id.toString()), null, null, null)

        Log.d(TAG, "Query person by id = " + id + "; number of rows = " + cursor.count)

        return cursor
    }

    fun insert(person: Person): Long {
        val contentValues = ContentValues()
        contentValues.put(PersonContract.COLUMN_NAME, person.name)

        val id = db.insert(PersonContract.TABLE_NAME, null, contentValues)

        Log.d(TAG, "Insert person = $person; row id = $id")

        return id
    }

    fun update(person: Person): Int {
        val contentValues = ContentValues()
        contentValues.put(PersonContract.COLUMN_NAME, person.name)

        val affectedRows = db.update(PersonContract.TABLE_NAME, contentValues,
                PersonContract._ID + " = ?", arrayOf(person.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Update person " + person)
        } else {
            Log.e(TAG, "Cannot update person " + person)
        }

        return affectedRows
    }

    fun delete(person: Person): Int {
        val affectedRows = db.delete(PersonContract.TABLE_NAME,
                PersonContract._ID + " = ?", arrayOf(person.id!!.toString()))

        if (affectedRows == 1) {
            Log.d(TAG, "Delete person " + person)
        } else {
            Log.e(TAG, "Cannot delete person " + person)
        }

        return affectedRows
    }

    companion object {
        private val TAG = PersonTable::class.java.simpleName
    }
}

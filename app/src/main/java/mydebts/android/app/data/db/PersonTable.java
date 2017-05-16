package mydebts.android.app.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import mydebts.android.app.data.model.Person;

public class PersonTable {
    private static final String TAG = PersonTable.class.getSimpleName();

    private final SQLiteDatabase db;

    @Inject
    public PersonTable(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor queryAll() {
        Cursor cursor = db.query(PersonContract.TABLE_NAME, null, null, null, null, null, null);

        Log.d(TAG, "Query all persons; number of rows = " + cursor.getCount());

        return cursor;
    }

    public Cursor queryById(@NonNull Long id) {
        Cursor cursor = db.query(PersonContract.TABLE_NAME, null,
                PersonContract._ID + " = ?", new String[] { id.toString() },
                null, null, null);

        Log.d(TAG, "Query person by id = " + id + "; number of rows = " + cursor.getCount());

        return cursor;
    }

    public long insert(@NonNull Person person) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.COLUMN_NAME, person.getName());

        long id = db.insert(PersonContract.TABLE_NAME, null, contentValues);

        Log.d(TAG, "Insert person = " + person + "; row id = " + id);

        return id;
    }

    public int update(@NonNull Person person) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.COLUMN_NAME, person.getName());

        int affectedRows = db.update(PersonContract.TABLE_NAME, contentValues,
                PersonContract._ID + " = ?", new String[] { person.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Update person " + person);
        } else {
            Log.e(TAG, "Cannot update person " + person);
        }

        return affectedRows;
    }

    public int delete(@NonNull Person person) {
        int affectedRows = db.delete(PersonContract.TABLE_NAME,
                PersonContract._ID + " = ?", new String[] { person.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Delete person " + person);
        } else {
            Log.e(TAG, "Cannot delete person " + person);
        }

        return affectedRows;
    }
}

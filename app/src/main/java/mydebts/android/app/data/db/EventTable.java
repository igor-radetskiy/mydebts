package mydebts.android.app.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import mydebts.android.app.data.model.Event;

@Singleton
public class EventTable {
    private static final String TAG = EventTable.class.getSimpleName();

    private final SQLiteDatabase db;

    @Inject
    public EventTable(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor queryAll() {
        Cursor cursor = db.query(EventContract.TABLE_NAME, null, null, null, null, null, null);

        Log.d(TAG, "Query all events; number of rows = " + cursor.getCount());

        return cursor;
    }

    public Cursor queryById(@NonNull Long id) {
        Cursor cursor = db.query(EventContract.TABLE_NAME, null,
                EventContract._ID + " = ?", new String[] { id.toString() },
                null, null, null);

        Log.d(TAG, "Query event by id = " + id + "; number of rows = " + cursor.getCount());

        return cursor;
    }

    public long insert(@NonNull Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.COLUMN_NAME, event.getName());
        contentValues.put(EventContract.COLUMN_DATE, event.getDate().getTime());

        long id = db.insert(EventContract.TABLE_NAME, null, contentValues);

        Log.d(TAG, "Insert event = " + event + "; row id = " + id);

        return id;
    }

    public int update(@NonNull Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.COLUMN_NAME, event.getName());
        contentValues.put(EventContract.COLUMN_DATE, event.getDate().getTime());

        int affectedRows = db.update(EventContract.TABLE_NAME, contentValues,
                EventContract._ID + " = ?", new String[] { event.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Update event " + event);
        } else {
            Log.e(TAG, "Cannot update event " + event);
        }

        return affectedRows;
    }

    public int delete(@NonNull Event event) {
        int affectedRows = db.delete(EventContract.TABLE_NAME,
                EventContract._ID + " = ?", new String[] { event.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Delete event " + event);
        } else {
            Log.e(TAG, "Cannot delete event " + event);
        }

        return affectedRows;
    }
}

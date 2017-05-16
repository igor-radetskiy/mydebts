package mydebts.android.app.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import mydebts.android.app.data.model.Participant;

public class ParticipantTable {
    private static final String TAG = ParticipantTable.class.getSimpleName();

    private final SQLiteDatabase db;

    @Inject
    public ParticipantTable(SQLiteDatabase db) {
        this.db = db;
    }

    public Cursor queryByEventId(@NonNull Long eventId) {
        Cursor cursor = query(ParticipantContract.COLUMN_EVENT_ID + " = ?", new String[] {eventId.toString()});

        Log.d(TAG, "Query participants by event id; eventId = " + eventId + "; number of rows = " + cursor.getCount());

        return cursor;
    }

    public Cursor queryByPersonId(@NonNull Long personId) {
        Cursor cursor = query(ParticipantContract.COLUMN_PERSON_ID + " = ?", new String[] {personId.toString()});

        Log.d(TAG, "Query participants by person id; personId = " + personId + "; number of rows = " + cursor.getCount());

        return cursor;
    }


    public long insert(@NonNull Participant participant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ParticipantContract.COLUMN_EVENT_ID, participant.getEvent().getId());
        contentValues.put(ParticipantContract.COLUMN_PERSON_ID, participant.getPerson().getId());
        contentValues.put(ParticipantContract.COLUMN_DEBT, participant.getDebt());

        long id = db.insert(ParticipantContract.TABLE_NAME, null, contentValues);

        Log.d(TAG, "Insert participant = " + participant + "; row id = " + id);

        return id;
    }

    public int update(@NonNull Participant participant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ParticipantContract.COLUMN_EVENT_ID, participant.getEvent().getId());
        contentValues.put(ParticipantContract.COLUMN_PERSON_ID, participant.getPerson().getId());
        contentValues.put(ParticipantContract.COLUMN_DEBT, participant.getDebt());

        int affectedRows = db.update(ParticipantContract.TABLE_NAME, contentValues,
                ParticipantContract._ID + " = ?", new String[] { participant.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Update participant " + participant);
        } else {
            Log.e(TAG, "Cannot update participant " + participant);
        }

        return affectedRows;
    }

    public int delete(@NonNull Participant participant) {
        int affectedRows = db.delete(ParticipantContract.TABLE_NAME,
                ParticipantContract._ID + " = ?", new String[] { participant.getId().toString() });

        if (affectedRows == 1) {
            Log.d(TAG, "Delete participant " + participant);
        } else {
            Log.e(TAG, "Cannot delete participant " + participant);
        }

        return affectedRows;
    }

    private Cursor query(String selection, String[] selectionArgs) {
        String joinTables = ParticipantContract.TABLE_NAME + " INNER JOIN " + EventContract.TABLE_NAME + " ON " +
                ParticipantContract.COLUMN_EVENT_ID + " = " + EventContract.TABLE_NAME + "." + EventContract._ID +
                " INNER JOIN " + PersonContract.TABLE_NAME + " ON " +
                ParticipantContract.COLUMN_PERSON_ID + " = " + PersonContract.TABLE_NAME + "." + PersonContract._ID;

        return db.query(joinTables, null, selection, selectionArgs, null, null, null);
    }
}

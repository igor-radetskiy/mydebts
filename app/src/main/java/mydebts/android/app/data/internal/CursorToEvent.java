package mydebts.android.app.data.internal;

import android.database.Cursor;

import java.util.Date;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.EventContract;
import mydebts.android.app.data.db.ParticipantContract;
import mydebts.android.app.data.model.Event;

class CursorToEvent implements Function<Cursor, Event> {
    @Override
    public Event apply(Cursor cursor) throws Exception {
        int idColumnIndex = cursor.getColumnIndex(ParticipantContract.COLUMN_EVENT_ID) >= 0 ?
                cursor.getColumnIndex(ParticipantContract.COLUMN_EVENT_ID) : cursor.getColumnIndex(EventContract._ID);

        return Event.builder()
                .id(cursor.getLong(idColumnIndex))
                .name(cursor.getString(cursor.getColumnIndex(EventContract.COLUMN_NAME)))
                .date(new Date(cursor.getLong(cursor.getColumnIndex(EventContract.COLUMN_DATE))))
                .build();
    }
}

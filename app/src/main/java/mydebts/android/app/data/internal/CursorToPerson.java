package mydebts.android.app.data.internal;

import android.database.Cursor;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.ParticipantContract;
import mydebts.android.app.data.db.PersonContract;
import mydebts.android.app.data.model.Person;

class CursorToPerson implements Function<Cursor, Person> {
    @Override
    public Person apply(Cursor cursor) throws Exception {
        int idColumnIndex = cursor.getColumnIndex(ParticipantContract.COLUMN_PERSON_ID) >= 0 ?
                cursor.getColumnIndex(ParticipantContract.COLUMN_PERSON_ID) : cursor.getColumnIndex(PersonContract._ID);

        return Person.builder()
                .id(cursor.getLong(idColumnIndex))
                .name(cursor.getString(cursor.getColumnIndex(PersonContract.COLUMN_NAME)))
                .build();
    }
}

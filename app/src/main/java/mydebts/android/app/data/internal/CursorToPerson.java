package mydebts.android.app.data.internal;

import android.database.Cursor;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.PersonContract;
import mydebts.android.app.data.model.Person;

class CursorToPerson implements Function<Cursor, Person> {
    @Override
    public Person apply(Cursor cursor) throws Exception {
        return Person.builder()
                .id(cursor.getLong(cursor.getColumnIndex(PersonContract._ID)))
                .name(cursor.getString(cursor.getColumnIndex(PersonContract.COLUMN_NAME)))
                .build();
    }
}

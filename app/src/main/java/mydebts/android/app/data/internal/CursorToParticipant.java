package mydebts.android.app.data.internal;

import android.database.Cursor;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.ParticipantContract;
import mydebts.android.app.data.model.Participant;

class CursorToParticipant implements Function<Cursor, Participant> {
    private static final CursorToEvent CURSOR_TO_EVENT = new CursorToEvent();
    private static final CursorToPerson CURSOR_TO_PERSON = new CursorToPerson();

    @Override
    public Participant apply(Cursor cursor) throws Exception {
        return Participant.builder()
                .id(cursor.getLong(cursor.getColumnIndex(ParticipantContract._ID)))
                .event(CURSOR_TO_EVENT.apply(cursor))
                .person(CURSOR_TO_PERSON.apply(cursor))
                .debt(cursor.getDouble(cursor.getColumnIndex(ParticipantContract.COLUMN_DEBT)))
                .build();
    }
}

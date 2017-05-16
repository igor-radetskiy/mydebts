package mydebts.android.app.data.db;

import android.provider.BaseColumns;

public interface ParticipantContract extends BaseColumns {
    String TABLE_NAME = "Participants";
    String COLUMN_PERSON_ID = "person_id";
    String COLUMN_EVENT_ID = "event_id";
    String COLUMN_DEBT = "debt";
}

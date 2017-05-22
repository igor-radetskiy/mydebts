package mydebts.android.app.data.db

interface ParticipantContract {
    companion object {
        val TABLE_NAME = "Participants"
        val _ID = "_id"
        val COLUMN_PERSON_ID = "person_id"
        val COLUMN_EVENT_ID = "event_id"
        val COLUMN_DEBT = "debt"
    }
}

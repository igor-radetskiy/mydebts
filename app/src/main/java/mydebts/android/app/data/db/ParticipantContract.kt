package mydebts.android.app.data.db

interface ParticipantContract {
    companion object {
        const val TABLE_NAME = "Participants"
        const val COLUMN_ID = "_id"
        const val COLUMN_PERSON_ID = "person_id"
        const val COLUMN_EVENT_ID = "event_id"
        const val COLUMN_DEBT = "debt"
    }
}

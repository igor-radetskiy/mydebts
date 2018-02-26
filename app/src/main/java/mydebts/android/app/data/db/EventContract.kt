package mydebts.android.app.data.db

interface EventContract {
    companion object {
        const val TABLE_NAME = "Events"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "event_name"
        const val COLUMN_DATE = "date"
    }
}

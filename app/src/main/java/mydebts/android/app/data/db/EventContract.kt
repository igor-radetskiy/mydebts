package mydebts.android.app.data.db

interface EventContract {
    companion object {
        val TABLE_NAME = "Events"
        val _ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_DATE = "date"
    }
}

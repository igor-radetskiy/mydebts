package mydebts.android.app.data.db

interface PersonContract {
    companion object {
        const val TABLE_NAME = "Persons"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "person_name"
    }
}

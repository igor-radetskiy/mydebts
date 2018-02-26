package mydebts.android.app.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MyDebtsDbOpenHelper @Inject
constructor(context: Context) : SQLiteOpenHelper(context, MyDebtsDbOpenHelper.DB_NAME, null, MyDebtsDbOpenHelper.DB_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON")

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + EventContract.TABLE_NAME + " ( " +
                        EventContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EventContract.COLUMN_NAME + " TEXT, " +
                        EventContract.COLUMN_DATE + " INTEGER NOT NULL)")

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + PersonContract.TABLE_NAME + " ( " +
                        PersonContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PersonContract.COLUMN_NAME + " TEXT NOT NULL)")

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + ParticipantContract.TABLE_NAME + " ( " +
                        ParticipantContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ParticipantContract.COLUMN_EVENT_ID + " INTEGER NOT NULL " +
                        "REFERENCES " + EventContract.TABLE_NAME + " ( " + EventContract.COLUMN_ID + " ) ON DELETE CASCADE, " +
                        ParticipantContract.COLUMN_PERSON_ID + " INTEGER NOT NULL " +
                        "REFERENCES " + PersonContract.TABLE_NAME + " ( " + PersonContract.COLUMN_ID + " ) ON DELETE CASCADE, " +
                        ParticipantContract.COLUMN_DEBT + " REAL NOT NULL)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE " + ParticipantContract.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE " + EventContract.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE " + PersonContract.TABLE_NAME)

        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val DB_NAME = "my_debts.db"
        private const val DB_VERSION = 2
    }
}

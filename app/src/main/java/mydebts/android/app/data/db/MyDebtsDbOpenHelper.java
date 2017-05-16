package mydebts.android.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MyDebtsDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "my_debts.db";
    private static final int DB_VERSION = 1;

    @Inject
    public MyDebtsDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + EventContract.TABLE_NAME + " ( " +
                        EventContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EventContract.COLUMN_NAME + " TEXT, " +
                        EventContract.COLUMN_DATE + " INTEGER NOT NULL)");

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + PersonContract.TABLE_NAME + " ( " +
                        PersonContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PersonContract.COLUMN_NAME + " TEXT NOT NULL)");

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + ParticipantContract.TABLE_NAME + " ( " +
                        ParticipantContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ParticipantContract.COLUMN_EVENT_ID + " INTEGER NOT NULL " +
                            "REFERENCES " + EventContract.TABLE_NAME + " ( " + EventContract._ID + " ) ON DELETE CASCADE, " +
                        ParticipantContract.COLUMN_PERSON_ID + " INTEGER NOT NULL " +
                            "REFERENCES " + PersonContract.TABLE_NAME + " ( " + PersonContract._ID + " ) ON DELETE CASCADE, " +
                        ParticipantContract.COLUMN_DEBT + " REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + ParticipantContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE " + EventContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE " + PersonContract.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}

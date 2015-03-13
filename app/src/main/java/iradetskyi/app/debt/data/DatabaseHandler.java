package iradetskyi.app.debt.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "debts";
	public static final int DATABASE_VERSION = 1;
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON;");
		db.execSQL(Buddy.CREATE_TABLE);
		db.execSQL(Buddy.INSERT_DEFAULT_USER);
		db.execSQL(Event.CREATE_TABLE);
		db.execSQL(PersonalDebt.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}

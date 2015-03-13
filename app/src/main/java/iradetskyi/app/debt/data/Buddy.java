package iradetskyi.app.debt.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Buddy {
	
	public static final String TAG = "Buddy";
	public static final String CREATE_TABLE = 
			"create table if not exists " + BuddyContract.TABLE_NAME + " ( " +
					BuddyContract.ID + " integer primary key autoincrement, " +
					BuddyContract.NAME + " text not null " +
			" ); ";
	
	public static final String INSERT_DEFAULT_USER = 
			" insert into " + BuddyContract.TABLE_NAME + 
			" ( " + BuddyContract.NAME + " ) " + 
			" values ('me'); ";
	
	private SQLiteDatabase mDatabase;
	
	public Buddy (SQLiteOpenHelper dbHelper) {
		if (dbHelper != null) {
			mDatabase = dbHelper.getWritableDatabase();
		}
	}
	
	public long insert(String name) {
		Log.d(TAG, "insert: " + name);
		if (mDatabase != null) {
			ContentValues values = new ContentValues();
			values.put(BuddyContract.NAME, name);
			long id = mDatabase.insert(BuddyContract.TABLE_NAME, null, values);
			Log.d(TAG, "insert: id = " + id);
			return id;
		}
		return -1;
	}
	
	public boolean update(int id) {
		return true;
	}
	
	public boolean delete(int id) {
		return true;
	}
	
	public Cursor read(long id) {
		Cursor retCursor = null;
		if (mDatabase != null) {
			String[] columns = {
					BuddyContract.ID,
					BuddyContract.NAME
			};
			retCursor = mDatabase.query(
					BuddyContract.TABLE_NAME, 
					columns, 
					BuddyContract.ID + "=?", 
					new String[] { Long.toString(id) }, 
					null, null, null);
		}
		return retCursor;
	}
	
	public Cursor readAll() {
		Cursor retCursor = null;
		if (mDatabase != null) {
			String[] columns = {
					BuddyContract.ID,
					BuddyContract.NAME
			};
			retCursor = mDatabase.query(BuddyContract.TABLE_NAME, columns, null, null, null, null, null);
		}
		return retCursor;
	}
	
	public Cursor readExceptFor(long[] exceptionId) {
		Cursor retCursor = null;
		if (mDatabase != null) {
			String[] columns = {
					BuddyContract.ID,
					BuddyContract.NAME
			};
			StringBuilder whereClause = new StringBuilder();
			String[] whereArgs = new String[exceptionId.length];
			for (int i = 0; i < exceptionId.length; i++) {
				if (i == exceptionId.length - 1) {
					whereClause.append(BuddyContract.ID + " !=?");
				} else {
					whereClause.append(BuddyContract.ID + " !=? or ");
				}
				whereArgs[i] = Long.toString(exceptionId[i]);
			}
			retCursor = mDatabase.query(
					BuddyContract.TABLE_NAME,
					columns,
					whereClause.toString(),
					whereArgs,
					null, null, null);
		}
		return retCursor;
	}

	public static class BuddyContract {
		public static final String TABLE_NAME = "Buddy";
		public static final String ID = "_id";
		public static final String NAME = "name";
	}
}

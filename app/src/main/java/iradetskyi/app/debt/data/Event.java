package iradetskyi.app.debt.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Event {
	
	public static final String TAG = "Event";
	public static final String CREATE_TABLE = 
			"create table if not exists " + EventContract.TABLE_NAME + " ( " +
					EventContract.ID + " integer primary key autoincrement, " +
					EventContract.TITLE + " text not null, " +
					EventContract.DATE + " text not null, " +
					EventContract.COST + " real not null " +
			" ); ";
	private SQLiteDatabase mDatabase;
	
	public Event (SQLiteOpenHelper dbHelper) {
		if (dbHelper != null) {
			mDatabase = dbHelper.getWritableDatabase();
		}
	}
	
	public long insert(String title, String date, float cost) {
		Log.d(TAG, "insert: " + title + ", " + date + ", " + cost);
		long id = -1;
		if (mDatabase != null) {
			ContentValues values = new ContentValues();
			values.put(EventContract.TITLE, title);
			values.put(EventContract.DATE, date);
			values.put(EventContract.COST, cost);
			id = mDatabase.insert(EventContract.TABLE_NAME, null, values);
		}
		return id;
	}

	public int updateTitle(long id, String title) {
		int affectedRows = -1;
		if (mDatabase != null) {
			ContentValues values = new ContentValues();
			values.put(EventContract.TITLE, title);
			affectedRows = mDatabase.update(
					EventContract.TABLE_NAME,
					values,
					EventContract.ID + "=?",
					new String[]{Long.toString(id)});
		}
		Log.d(TAG, "updateTitle: id = " + id + " title = " + title + " affectedRows = " + affectedRows);
		return affectedRows;
	}
	
	public int updateDate(long id, String date) {
		int affectedRows = -1;
		if (mDatabase != null) {
			ContentValues values = new ContentValues();
			values.put(EventContract.DATE, date);
			affectedRows = mDatabase.update(
					EventContract.TABLE_NAME,
					values,
					EventContract.ID + "=?",
					new String[]{Long.toString(id)});
		}
		Log.d(TAG, "updateTitle: id = " + id + " date = " + date + " affectedRows = " + affectedRows);
		return affectedRows;
	}
	
	public int delete(long id) {
		int affectedRows = -1;
		if (mDatabase != null) {
			affectedRows = mDatabase.delete(
					EventContract.TABLE_NAME, 
					EventContract.ID + "=?", 
					new String[] {Long.toString(id)});
		}
		return affectedRows;
	}
	
	public Cursor read(long id) {
		Cursor retCursor = null;
		if (mDatabase != null) {
			String[] columns = {
					EventContract.ID, 
					EventContract.TITLE,
					EventContract.DATE,
					EventContract.COST
			};
			retCursor = mDatabase.query(
					EventContract.TABLE_NAME, 
					columns, 
					EventContract.ID + "=?", 
					new String[]{Long.toString(id)}, 
					null, null, null);
		}
		return retCursor;
	}
	
	public Cursor readAll() {
		Cursor retCursor = null;
		if (mDatabase != null) {
			String[] columns = {
					EventContract.ID, 
					EventContract.TITLE,
					EventContract.DATE,
					EventContract.COST
			};
			retCursor = mDatabase.query(EventContract.TABLE_NAME, columns, null, null, null, null, null);
		}
		return retCursor;
	}
	
	public static class EventContract {
		public static final String TABLE_NAME = "Event";
		public static final String ID = "_id";
		public static final String TITLE = "title";
		public static final String DATE = "date";
		public static final String COST = "cost";
	}
	
	public static class EventModel {
		private int mId;
		private String mTitle;
		private String mDate;
		private int mCost;
		
		private EventModel(int id, String title, String date, int cost) {
			mId = id;
			mTitle = title;
			mDate = date;
			mCost = cost;
		}
		
		public int getId() { 
			return mId;
		}
		
		public String getTitle() { 
			return mTitle;
		}
		
		public String getDate() { 
			return mDate;
		}
		
		public int getCost() { 
			return mCost;
		}
	}
}

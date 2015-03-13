package iradetskyi.app.debt.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.Event.EventContract;

public class PersonalDebt {
	
	public static final String TAG = "PersonalDebt";
	public static final String CREATE_TABLE = 
			"create table if not exists " + PersonalDebtContract.TABLE_NAME + " ( " +
					PersonalDebtContract.ID + " integer primary key autoincrement, " +
					PersonalDebtContract.EVENT_ID + " integer not null " +
						" references " + EventContract.TABLE_NAME + " ( " + EventContract.ID + " ) " + 
						" on update cascade on delete cascade, " +
					PersonalDebtContract.BUDDY_ID + " integer not null " +
						" references " + BuddyContract.TABLE_NAME + " ( " + BuddyContract.ID + " ) " + 
						" on update cascade on delete restrict, " +
					PersonalDebtContract.CREDITOR_ID + " integer not null " +
						" references " + BuddyContract.TABLE_NAME + " ( " + BuddyContract.ID + " ) " + 
						" on update cascade on delete restrict, " +
					PersonalDebtContract.BUDDY_DEBT + " real not null " +
			" ); ";
	
	private SQLiteDatabase mDatabase;
	
	public PersonalDebt (SQLiteOpenHelper dbHelper) {
		if (dbHelper != null) {
			mDatabase = dbHelper.getWritableDatabase();
		}
	}
	
	public long insert(long eventId, long buddyId, long creditorId, float buddyDebt) {
		Log.d(TAG, "insert: " + eventId + ", " + buddyId + ", " + buddyDebt);
		if (mDatabase != null) {
			ContentValues values = new ContentValues();
			values.put(PersonalDebtContract.EVENT_ID, eventId);
			values.put(PersonalDebtContract.BUDDY_ID, buddyId);
			values.put(PersonalDebtContract.CREDITOR_ID, creditorId);
			values.put(PersonalDebtContract.BUDDY_DEBT, buddyDebt);
			long id = mDatabase.insert(PersonalDebtContract.TABLE_NAME, null, values);
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
	
	public Cursor readByEventId(long eventId) {
		Cursor cursor = null;
		if (mDatabase != null) {
			String[] columns = {
					PersonalDebtContract.BUDDY_ID,
					PersonalDebtContract.BUDDY_DEBT,
					PersonalDebtContract.CREDITOR_ID
			};
			cursor = mDatabase.query(
					PersonalDebtContract.TABLE_NAME, 
					columns,
					PersonalDebtContract.EVENT_ID + "=?", 
					new String[] {Long.toString(eventId)}, 
					null, null, null);
		}
		return cursor;
	}
	
	public Cursor readDebts(long userId) {
		Cursor cursor = null;
		if (mDatabase != null) {
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(
					PersonalDebtContract.TABLE_NAME + 
					" inner join " + 
					EventContract.TABLE_NAME + 
					" on " + PersonalDebtContract.EVENT_ID + "=" + EventContract.TABLE_NAME + "." + EventContract.ID + 
					" inner join " + 
					BuddyContract.TABLE_NAME + 
					" on " + PersonalDebtContract.CREDITOR_ID + "=" + BuddyContract.TABLE_NAME + "." + BuddyContract.ID);
			cursor = builder.query(
					mDatabase, 
					new String[] {PersonalDebtContract.TABLE_NAME + "." + PersonalDebtContract.ID, EventContract.TITLE, EventContract.DATE, PersonalDebtContract.BUDDY_DEBT, PersonalDebtContract.CREDITOR_ID, BuddyContract.NAME}, 
					PersonalDebtContract.BUDDY_ID + "=?", 
					new String[] {Long.toString(userId)}, 
					null, null, null);
		}
		return cursor;
	}
	
	public Cursor readCredits(long userId) {
		Cursor cursor = null;
		if (mDatabase != null) {
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(
					PersonalDebtContract.TABLE_NAME + 
					" inner join " + 
					EventContract.TABLE_NAME + 
					" on " + PersonalDebtContract.EVENT_ID + "=" + EventContract.TABLE_NAME + "." + EventContract.ID + 
					" inner join " + 
					BuddyContract.TABLE_NAME + 
					" on " + PersonalDebtContract.BUDDY_ID + "=" + BuddyContract.TABLE_NAME + "." + BuddyContract.ID);
			cursor = builder.query(mDatabase, 
					new String[] {PersonalDebtContract.TABLE_NAME + "." + PersonalDebtContract.ID, EventContract.TITLE, EventContract.DATE, PersonalDebtContract.BUDDY_DEBT, PersonalDebtContract.BUDDY_ID, BuddyContract.NAME}, 
					PersonalDebtContract.CREDITOR_ID + "=?", 
					new String[] {Long.toString(userId)}, 
					null, null, null);
		}
		return cursor;
	}
	
	public static class PersonalDebtContract {
		public static final String TABLE_NAME = "PersonalDebt";
		public static final String ID = "_id";
		public static final String EVENT_ID = "event_id";
		public static final String BUDDY_ID = "buddy_id";
		public static final String CREDITOR_ID = "creditor_id";
		public static final String BUDDY_DEBT = "buddy_debt";
		
	}
}

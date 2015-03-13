package iradetskyi.app.debt.data;

import android.content.Context;
import android.database.Cursor;

public class BuddyLoader extends DataLoader {

	public static final int LOAD_ALL_BUDDIES = 0;
	public static final int LOAD_ALL_BUDDIES_EXCEPT_FOR_ME = 1;

	private Buddy mBuddy;
	private int mFlag = LOAD_ALL_BUDDIES;
	
	public BuddyLoader(Context context, int flag) {
		super(context);
		
		mBuddy = new Buddy(new DatabaseHandler(context));
		mFlag = flag;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor retCursor = null;
		switch (mFlag) {
		case LOAD_ALL_BUDDIES:
			retCursor = loadAllBuddies();
			break;
		case LOAD_ALL_BUDDIES_EXCEPT_FOR_ME:
			retCursor = loadAllBuddiesExceptForMe();
			break;
		default:
			break;
		}
		return retCursor;
	}

	private Cursor loadAllBuddies() {
		return mBuddy.readAll();
	}

	private Cursor loadAllBuddiesExceptForMe() {
		return mBuddy.readExceptFor(new long[]{1});
	}
}

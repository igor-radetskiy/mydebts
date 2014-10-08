package iradetskyi.app.debt.data;

import android.content.Context;
import android.database.Cursor;

public class BuddyLoader extends DataLoader {

	private Buddy mBuddy;
	
	public BuddyLoader(Context context) {
		super(context);
		
		mBuddy = new Buddy(new DatabaseHandler(context));
	}

	@Override
	public Cursor loadInBackground() {
		Cursor retCursor = loadAllBuddies();
		return retCursor;
	}

	private Cursor loadAllBuddies() {
		return mBuddy.readAll();
	}

}

package iradetskyi.app.debt.data;

import android.content.Context;
import android.database.Cursor;

public class DebtLoader extends DataLoader {
	public static final int CREDIT_FLAG = 0;
	public static final int DEBT_FLAG = 1;
	
	private PersonalDebt mPersonalDebt;
	private Long mBuddyId;
	private int mFlag;
	
	public DebtLoader(Context context, long buddyId, int flag) {
		super(context);
		
		DatabaseHandler dbHelper = new DatabaseHandler(context.getApplicationContext());
		mPersonalDebt = new PersonalDebt(dbHelper);
		mBuddyId = buddyId;
		mFlag = flag;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor result = null;
		switch (mFlag) {
		case CREDIT_FLAG:
			result = loadAllCredits();
			break;
		case DEBT_FLAG:
			result = loadAllDebts();
			break;
		default:
			break;
		}
		return result;
	}
	
	private Cursor loadAllCredits() {
		return mPersonalDebt.readCredits(mBuddyId);
	}
	
	private Cursor loadAllDebts() {
		return mPersonalDebt.readDebts(mBuddyId);
	}
}

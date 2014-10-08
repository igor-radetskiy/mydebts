package iradetskyi.app.debt.fragment;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.data.DebtLoader;
import iradetskyi.app.debt.data.Event.EventContract;
import iradetskyi.app.debt.data.PersonalDebt.PersonalDebtContract;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class BuddyFragment extends Fragment {
	
	public static final String BUDDY_ID_EXTRA = "iradetskyi.app.debt.extra.BUDDY_ID_EXTRA";
	
	private static final int CREDIT_LOADER_ID = 0;
	private static final int DEBT_LOADER_ID = 1;
	
	private long mBuddyId = 1;
	private String mBuddyName;
	
	private ListView mCreditList;
	private ListView mDebtList;
	
	private SimpleCursorAdapter mCreditAdapter;
	private SimpleCursorAdapter mDebtAdapter;
	
	private TextView mTvUserName;
	
	private LoaderManager.LoaderCallbacks<Cursor> mDebtLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
		
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			switch (loader.getId()) {
			case CREDIT_LOADER_ID:
				mCreditAdapter.changeCursor(null);
				break;
			case DEBT_LOADER_ID:
				mDebtAdapter.changeCursor(null);
				break;
			default:
				break;
			}
		}
		
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			switch (loader.getId()) {
			case CREDIT_LOADER_ID:
				mCreditAdapter.changeCursor(cursor);
				break;
			case DEBT_LOADER_ID:
				mDebtAdapter.changeCursor(cursor);
				break;
			default:
				break;
			}
		}
		
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Loader<Cursor> loader = null;
			switch (id) {
			case CREDIT_LOADER_ID:
				loader = new DebtLoader(getActivity().getApplicationContext(), mBuddyId, DebtLoader.CREDIT_FLAG);
				break;
			case DEBT_LOADER_ID:
				loader = new DebtLoader(getActivity().getApplicationContext(), mBuddyId, DebtLoader.DEBT_FLAG);
				break;
			default:
				break;
			}
			return loader;
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			mBuddyId = args.getLong(BUDDY_ID_EXTRA);
		}
		getBuddyNameFromDb();
		
		View view = inflater.inflate(R.layout.fragment_buddy, container, false);
		mCreditList = (ListView)view.findViewById(R.id.fragment_buddy_lv_credits);
		mDebtList = (ListView)view.findViewById(R.id.fragment_buddy_lv_debts);
		mTvUserName = (TextView)view.findViewById(R.id.fragment_buddy_tv_user_name);
		
		displayUserName();
		initLists();
		
		getLoaderManager().initLoader(CREDIT_LOADER_ID, null, mDebtLoaderCallbacks);
		getLoaderManager().initLoader(DEBT_LOADER_ID, null, mDebtLoaderCallbacks);
		
		return view;
	}
	
	private void getBuddyNameFromDb() {
		Buddy buddy = new Buddy(new DatabaseHandler(getActivity().getApplicationContext()));
		Cursor cursor = buddy.read(mBuddyId);
		if (cursor != null && cursor.moveToFirst()) {
			mBuddyName = cursor.getString(cursor.getColumnIndex(BuddyContract.NAME));
		}
	}
	
	private void displayUserName() {
		if (mTvUserName != null && mBuddyName != null) {
			mTvUserName.setText(mBuddyName);
		}
	}
	
	private void initLists() {
		mCreditAdapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), 
				R.layout.personal_debt_item, 
				null, 
				new String[] {EventContract.TITLE, EventContract.DATE, PersonalDebtContract.BUDDY_DEBT, BuddyContract.NAME}, 
				new int[] {R.id.personal_debt_item_event, R.id.personal_debt_item_date, R.id.personal_debt_item_debt, R.id.personal_debt_item_creditor}, 
				0);
		mDebtAdapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), 
				R.layout.personal_debt_item, 
				null, 
				new String[] {EventContract.TITLE, EventContract.DATE, PersonalDebtContract.BUDDY_DEBT, BuddyContract.NAME}, 
				new int[] {R.id.personal_debt_item_event, R.id.personal_debt_item_date, R.id.personal_debt_item_debt, R.id.personal_debt_item_creditor}, 
				0);
		
		mCreditList.setAdapter(mCreditAdapter);
		mDebtList.setAdapter(mDebtAdapter);
	}
}

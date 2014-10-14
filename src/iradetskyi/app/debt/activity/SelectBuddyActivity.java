package iradetskyi.app.debt.activity;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy;
import iradetskyi.app.debt.data.BuddyLoader;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.list.adapter.SelectBuddyAdapter;
import iradetskyi.app.debt.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class SelectBuddyActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

	public static final String SELECTED_BUDDIES_EXTRA = "selectedBuddiesExtra";
	public static final String CREDITORS_EXTRA = "creditorsExtra";
	public static final String PAYMENTS_EXTRA = "paymentsExtra";
	
	private static final int BUDDY_LOADER_ID = 0;
	
	private CheckBox mSelectAll;
	private OnClickListener mSelectAllListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v == mSelectAll) {
				if (mSelectAll.isChecked()) {
					mAdapter.selectAll();
				} else {
					mAdapter.unselectAll();
				}
			}
		}
	};
	
	private ListView mBuddyList;
	private SelectBuddyAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_buddy);
		
		mSelectAll = (CheckBox)findViewById(R.id.cb_select_all);
		mSelectAll.setOnClickListener(mSelectAllListener);
		
		mBuddyList = (ListView)findViewById(R.id.lv_buddy_list);
		mBuddyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				mAdapter.selectItemById(id);
				mSelectAll.setChecked(mAdapter.areAllItemsSelected());
			}
			
		});
		
		mAdapter = setupAdapter();
		mBuddyList.setAdapter(mAdapter);
		getLoaderManager().initLoader(BUDDY_LOADER_ID, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.buddy, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_buddy:
			startNewByddyDialog();
			break;
		case R.id.action_done:
			returnBuddyList();
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
		Loader<Cursor> retLoader = null;
		if (loaderId == BUDDY_LOADER_ID) {
			retLoader = new BuddyLoader(getApplicationContext(), BuddyLoader.LOAD_ALL_BUDDIES);
		}
		return retLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mAdapter.changeCursor(c);
		mSelectAll.setChecked(mAdapter.areAllItemsSelected());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
	
	private SelectBuddyAdapter setupAdapter() {
		SelectBuddyAdapter adapter = new SelectBuddyAdapter(this, null);
		Intent intent = getIntent();
		if (intent != null) {
			long[] ids = intent.getLongArrayExtra(SELECTED_BUDDIES_EXTRA);
			if (ids != null && ids.length > 0) {
				for (long id : ids) {
					adapter.selectItemById(id);
				}
			}
		}
		return adapter;
	}
	
	private void restartLoader() {
		getLoaderManager().restartLoader(BUDDY_LOADER_ID, null, this);
	}
	
	private void startNewByddyDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_new_buddy_title);
		final EditText etBuddyName = new EditText(this);
		builder.setView(etBuddyName);
		builder.setPositiveButton(R.string.dialog_common_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = etBuddyName.getText().toString();
				insertBuddyToDb(name);
				restartLoader();
			}
			
			private long insertBuddyToDb(String name) {
				long id = -1;
				Buddy buddy = new Buddy(new DatabaseHandler(getApplicationContext()));
				if (name != null && name.length() > 0) {
					id = buddy.insert(name);
				}
				return id;
			}
		});
		builder.create().show();
	}
	
	private void returnBuddyList() {
		long[] buddyList = Utils.toLongArray(mAdapter.getSelection());
		SparseArray<Float> creditors = mAdapter.getPayments();
		long[] creditorList = new long[creditors.size()];
		float[] paymentList = new float[creditors.size()];
		
		int j = 0;
		for (int i = 0; i < buddyList.length; i++) {
			float payment = creditors.get((int)buddyList[i], -1.0f);
			if (payment != -1.0f) {
				creditorList[j] = buddyList[i];
				paymentList[j] = payment;
				j++;
			}
		}
		
		Intent intent = new Intent();
		intent.putExtra(SELECTED_BUDDIES_EXTRA, buddyList);
		intent.putExtra(CREDITORS_EXTRA, creditorList);
		intent.putExtra(PAYMENTS_EXTRA, paymentList);
		
		setResult(RESULT_OK, intent);
		finish();
		/*SparseArray<Float> debtors = new SparseArray<Float>();
		float fullPayment = mAdapter.getFullPayment();
		float fullPartPayment = (float) (fullPayment / buddyList.length);
		float fullDebt = 0.0f;
		
		for (long userId : buddyList) {
			float payment = fullPartPayment - creditors.get((int)userId, 0.0f);
			if (payment > 0) {
				fullDebt += payment;
				debtors.put((int)userId, payment);
				creditors.remove((int)userId);
			}
		}
		
		PersonalDebt pd = new PersonalDebt(new DatabaseHandler(this));
		for (long userId : buddyList) {
			float payment = debtors.get((int)userId, 0.0f);
			if (payment != 0.0) {
				for (long creditorId : buddyList) {
					float credit = creditors.get((int)creditorId, 0.0f) - fullPartPayment;
					if (credit > 0.0) {
						Log.d("ddebug", userId + " " + creditorId + " " + credit * payment / fullDebt);
						//pd.insert(0L, userId, creditorId, credit * payment / fullDebt);
					}
				}
			}
		}*/
	}
}

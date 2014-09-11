package iradetskyi.app.debt.activity;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy;
import iradetskyi.app.debt.data.BuddyLoader;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.list.adapter.SelectionCursorAdapter;
import iradetskyi.app.debt.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
	private SelectionCursorAdapter mAdapter;
	
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
			retLoader = new BuddyLoader(getApplicationContext());
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
	
	private SelectionCursorAdapter setupAdapter() {
		String[] from = {BuddyContract.NAME};
		int[] to = {R.id.select_buddy_item_tv_name};
		int listItemResourceId = R.layout.select_buddy_item;
		SelectionCursorAdapter adapter = new SelectionCursorAdapter(getApplicationContext(), listItemResourceId, null, from, to, 0, R.id.select_buddy_item_checkbox);
		
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
		
		Intent intent = new Intent();
		intent.putExtra(SELECTED_BUDDIES_EXTRA, buddyList);
		setResult(RESULT_OK, intent);
		finish();
	}
}

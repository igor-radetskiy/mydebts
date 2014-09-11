package iradetskyi.app.debt.fragment;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.activity.EventActivity;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.data.Event;
import iradetskyi.app.debt.data.EventLoader;
import iradetskyi.app.debt.data.Event.EventContract;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EventListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final int LOADER_ID_EVENTS = 0;
	private static final int REQUEST_CODE_CHANGE_EVENT = 0;
	
	private ListView mHistoryList;
	private SimpleCursorAdapter mCursorAdapter;
	
	private ActionMode mActionMode;
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.contextual_history, menu);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Long eventId = (Long) mActionMode.getTag();
			switch (item.getItemId()) {
			case R.id.contextual_menu_history_view:
				startViewEvent(eventId);
				break;
			case R.id.contextual_menu_history_edit:
				startEditEvent(eventId);
				break;
			case R.id.contextual_menu_history_delete:
				if (eventId != null) {
					deleteEvent(eventId);
				}
				break;
			default:
				break;
			}
			mode.finish();
			return false;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_history, container, false);
		mHistoryList = (ListView)view.findViewById(R.id.lv_history);
		mCursorAdapter = setupCursorAdapter();
		mHistoryList.setAdapter(mCursorAdapter);
		mHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				Cursor cursor = mCursorAdapter.getCursor(); 
				if (cursor != null && cursor.moveToPosition(position)) {
					long eventId = cursor.getLong(cursor.getColumnIndex(EventContract.ID));
					startViewEvent(eventId);
				}
			}
		});
		mHistoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
				if (mActionMode != null) {
					return false;
				}
				
				Cursor cursor = mCursorAdapter.getCursor(); 
				if (cursor != null && cursor.moveToPosition(position)) {
					long eventId = cursor.getLong(cursor.getColumnIndex(EventContract.ID));
					mActionMode = getActivity().startActionMode(mActionModeCallback);
					mActionMode.setTag(eventId);
					view.setSelected(true);
					return true;
				} else {
					return false;
				}
			}
			

		});
		
		getLoaderManager().initLoader(LOADER_ID_EVENTS, null, this);
		setHasOptionsMenu(true);
		return view;
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_CHANGE_EVENT) {
				getLoaderManager().restartLoader(LOADER_ID_EVENTS, null, this);
			}
		}
	}
	
	private SimpleCursorAdapter setupCursorAdapter() {
		String[] from = {EventContract.TITLE, EventContract.DATE};
		int[] to = {android.R.id.text1, android.R.id.text2};
		int listItemResourceId = android.R.layout.simple_list_item_2;
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), listItemResourceId, null, from, to, 0);
		return adapter;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.history, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_event:
			startNewEvent();
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
		Loader<Cursor> retLoader = null;
		if (loaderId == LOADER_ID_EVENTS) {
			retLoader = new EventLoader(getActivity());
		}
		return retLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCursorAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.changeCursor(null);
	}
	
	private void startNewEvent() {
		Intent newEventIntent = new Intent(EventActivity.CREATE_EVENT_ACTION);
		startActivityForResult(newEventIntent, REQUEST_CODE_CHANGE_EVENT);
	}
	
	private void startViewEvent(long eventId) {
		Intent viewEventIntent = new Intent(EventActivity.VIEW_EVENT_ACTION);
		viewEventIntent.putExtra(EventActivity.EVENT_ID_EXTRA, eventId);
		startActivityForResult(viewEventIntent, REQUEST_CODE_CHANGE_EVENT);
	}
	
	private void startEditEvent(long eventId) {
		Intent editEventIntent = new Intent(EventActivity.EDIT_EVENT_ACTION);
		editEventIntent.putExtra(EventActivity.EVENT_ID_EXTRA, eventId);
		startActivityForResult(editEventIntent, REQUEST_CODE_CHANGE_EVENT);
	}
	
	private void deleteEvent(long eventId) {
		Event event = new Event(new DatabaseHandler(getActivity()));
		event.delete(eventId);
		getLoaderManager().restartLoader(LOADER_ID_EVENTS, null, this);
	}
}

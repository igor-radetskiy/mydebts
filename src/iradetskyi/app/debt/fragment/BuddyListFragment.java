package iradetskyi.app.debt.fragment;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.BuddyLoader;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BuddyListFragment extends Fragment implements LoaderCallbacks<Cursor> {
	private ListView mBuddyList;
	private SimpleCursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_buddy_list, container, false);
		mAdapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(),
				R.layout.list_item_fragment_buddy_list,
				null,
				new String[]{BuddyContract.NAME},
				new int[]{R.id.list_item_fragment_buddy_list_tv_buddy_name},
				0);
		mBuddyList = (ListView)view.findViewById(R.id.fragmnet_buddy_list_lv_buddy_list);
		mBuddyList.setAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);

		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Loader<Cursor> retLoader = new BuddyLoader(getActivity().getApplicationContext(), BuddyLoader.LOAD_ALL_BUDDIES_EXCEPT_FOR_ME);
		return retLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
}

package iradetskyi.app.debt.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

public abstract class DataLoader extends AsyncTaskLoader<Cursor> {

	private Cursor mCursor;
	
	public DataLoader(Context context) {
		super(context);
	}
	
	@Override
	public abstract Cursor loadInBackground();

	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;
		if (isStarted()) {
			super.deliverResult(cursor);
		}
		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}
}

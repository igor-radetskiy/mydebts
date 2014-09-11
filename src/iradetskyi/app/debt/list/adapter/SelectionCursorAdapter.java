package iradetskyi.app.debt.list.adapter;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

public class SelectionCursorAdapter extends SimpleCursorAdapter {

	private static final String ID = "_id";
	
	private int mLayoutId;
	private int mCheckBoxId;
	
	private Set<Long> mSelection = new HashSet<Long>();
	
	public SelectionCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, int checkBox) {
		super(context, layout, c, from, to, flags);
		
		mLayoutId = layout;
		mCheckBoxId = checkBox;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(mLayoutId, null);
		return v;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox cb = (CheckBox) view.findViewById(mCheckBoxId);		
		long id = cursor.getLong(cursor.getColumnIndex(ID));
		cb.setChecked(mSelection.contains(id));
		
		super.bindView(view, context, cursor);
	}
	
	@Override
	public long getItemId(int position){
		long id = -1;
		Cursor cursor = getCursor();
		if (cursor.moveToPosition(position)) {
			id = cursor.getLong(cursor.getColumnIndex(ID));
		}
		return id;
	}
	
	public void selectItem(int position) {
		long id = getItemId(position);
		selectItemById(id);
	}
	
	public void selectItemById(long id) {
		if (mSelection.contains(id)) {
			mSelection.remove(id);
		} else {
			mSelection.add(id);
		}
		notifyDataSetChanged();
	}
	
	public void selectAll() {
		Cursor cursor = getCursor();
		int idColumnIndex = cursor.getColumnIndex(ID);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			long id = cursor.getLong(idColumnIndex);
			mSelection.add(id);
		}
		notifyDataSetChanged();
	}
	
	public void unselectAll() {
		mSelection.clear();
		notifyDataSetChanged();
	}
	
	public boolean areAllItemsSelected() {
		Cursor cursor = getCursor();
		if (cursor == null) {
			return false;
		}
		int idColumnIndex = cursor.getColumnIndex(ID);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			long id = cursor.getLong(idColumnIndex);
			if (!mSelection.contains(id)){
				return false;
			}
		}
		return true;
	}
	
	public Set<Long> getSelection() {
		return mSelection;
	}
}

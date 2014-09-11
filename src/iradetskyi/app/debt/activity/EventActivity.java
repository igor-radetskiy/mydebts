package iradetskyi.app.debt.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.Event;
import iradetskyi.app.debt.data.Event.EventContract;
import iradetskyi.app.debt.data.PersonalDebt;
import iradetskyi.app.debt.data.PersonalDebt.PersonalDebtContract;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EventActivity extends Activity {

	public static final String CREATE_EVENT_ACTION = "iradetskyi.app.debt.action.CREATE_EVENT_ACTION";
	public static final String EDIT_EVENT_ACTION = "iradetskyi.app.debt.action.EDIT_EVENT_ACTION";
	public static final String VIEW_EVENT_ACTION = "iradetskyi.app.debt.action.VIEW_EVENT_ACTION";
	
	public static final String TITLE_EXTRA = "iradetskyi.app.debt.extra.TITLE_EXTRA";
	public static final String DATE_EXTRA = "iradetskyi.app.debt.extra.DATE_EXTRA";
	public static final String COST_EXTRA = "iradetskyi.app.debt.extra.COST_EXTRA";
	public static final String EVENT_ID_EXTRA = "iradetskyi.app.debt.extra.EVENT_ID_EXTRA";
	public static final String ACTION_EXTRA = "iradetskyi.app.debt.extra.ACTION_EXTRA";
	
	private static final int SELECT_BUDDY_REQUEST_CODE = 0;
	
	private String mTitle;
	private String mDate;
	private float mCost;
	private long[] mBuddyIdList;
	private long mEventId;
	
	private String mAction;
	
	private Menu mMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_info);
		
		if (savedInstanceState != null) {
			String action = savedInstanceState.getString(ACTION_EXTRA);
			setupView(action, getIntent().getExtras());
		} else {
			setupView(getIntent().getAction(), getIntent().getExtras());
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		displayEventData(
				savedInstanceState.getString(TITLE_EXTRA), 
				savedInstanceState.getString(DATE_EXTRA), 
				savedInstanceState.getFloat(COST_EXTRA), 
				savedInstanceState.getLongArray(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA));
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLongArray(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA, mBuddyIdList);
		outState.putString(TITLE_EXTRA, mTitle);
		outState.putString(DATE_EXTRA, mDate);
		outState.putString(ACTION_EXTRA, mAction);
		outState.putFloat(COST_EXTRA, mCost);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		mMenu = menu;
		if (mAction.equals(VIEW_EVENT_ACTION)) {
		
			menu.setGroupVisible(R.id.menu_group_event_view, true);
		} else {
			menu.setGroupVisible(R.id.menu_group_event_edit, true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_done:
			insertChangesToDb();
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.action_edit:
			transformForEdit();
			break;
		default:
			break;
		}
		return false;
	}

	public void startChangeTitleDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Set title");
		final EditText entry = new EditText(EventActivity.this);
		entry.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(entry).setPositiveButton(R.string.dialog_common_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface iface, int id) {
				((TextView)findViewById(R.id.title_info_content)).setText(entry.getText().toString());
			}
		});
		builder.create().show();
	}
	
	public void startDatePickerDialog(View v) {
		Date date = Calendar.getInstance().getTime();
		@SuppressWarnings("deprecation")
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
				String date = String.format("%d.%d.%d", dayOfMonth, monthOfYear + 1, year);
				((TextView)findViewById(R.id.date_info_content)).setText(date);
			}
		}, date.getYear(), date.getMonth(), date.getDay());
		dialog.getDatePicker().setCalendarViewShown(false);
		dialog.show();
	}
	
	public void startChangeCostDialog(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText entry = new EditText(EventActivity.this);
		entry.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		builder.setTitle("Set cost")
			   .setView(entry)
			   .setPositiveButton(R.string.dialog_common_ok, new DialogInterface.OnClickListener() {

				   @Override
				   public void onClick(DialogInterface arg0, int arg1) {
					   String cost  = entry.getText().toString();
					   ((TextView)findViewById(R.id.cost_info_content)).setText(cost);
				   }
			   })
			   .create().show();
		
	}
	
	public void startEditBuddyList(View v) {
		Intent addBuddy = new Intent(this, SelectBuddyActivity.class);
		addBuddy.putExtra(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA, mBuddyIdList);
		startActivityForResult(addBuddy, SELECT_BUDDY_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_BUDDY_REQUEST_CODE) {
				long[] selectedBuddies = data.getLongArrayExtra(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA);
				displaySelectedBuddies(selectedBuddies);
			}
		}
	}

	private void setupView(String action, Bundle bundle) {
		((TextView)findViewById(R.id.title_info_title)).setText(R.string.activity_event_item_title);
		((TextView)findViewById(R.id.date_info_title)).setText(R.string.date_info_title);
		((TextView)findViewById(R.id.cost_info_title)).setText(R.string.cost_info_title);
		((TextView)findViewById(R.id.buddies_info_title)).setText(R.string.buddies_info_title);

		mAction = action;
		if (mAction != null) {
			if (mAction.equals(CREATE_EVENT_ACTION)) {
				setupCreateActivity();
			} else if (mAction.equals(EDIT_EVENT_ACTION)) {
				setupEditActivity(bundle);
			} else if (mAction.equals(VIEW_EVENT_ACTION)) {
				setupViewActivity(bundle);
			}
		}
	}
	
	private void setupCreateActivity() {
		setTitle(R.string.activity_event_title_new_event);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
		String currentDate = df.format(calendar.getTime());
		displayEventData(
				getString(R.string.activity_event_item_default_title, currentDate), 
				currentDate, 
				0, 
				null);
	}
	
	private void setupEditActivity(Bundle bundle) {
		if (bundle != null) {
			setEventId(bundle.getLong(EVENT_ID_EXTRA));
		}
	}
	
	private void setupViewActivity(Bundle bundle) {
		if (bundle != null) {
			setEventId(bundle.getLong(EVENT_ID_EXTRA));
			
			findViewById(R.id.title_info).setClickable(false);
			findViewById(R.id.date_info).setClickable(false);
			findViewById(R.id.cost_info).setClickable(false);
			findViewById(R.id.buddies_info).setClickable(false);
		}
	}
	
	private void transformForEdit() {
		mAction = EDIT_EVENT_ACTION;
		
		mMenu.setGroupVisible(R.id.menu_group_event_edit, true);
		mMenu.setGroupVisible(R.id.menu_group_event_view, false);
		
		findViewById(R.id.title_info).setClickable(true);
		findViewById(R.id.date_info).setClickable(true);
		findViewById(R.id.cost_info).setClickable(true);
		findViewById(R.id.buddies_info).setClickable(true);
	}
	
	private void setEventId(long eventId) {
		mEventId = eventId;
		Event event = new Event(new DatabaseHandler(getApplicationContext()));
		Cursor cursor = event.read(mEventId);
		if (cursor != null && cursor.moveToFirst()) {
			String title = cursor.getString(cursor.getColumnIndex(EventContract.TITLE));
			String date = cursor.getString(cursor.getColumnIndex(EventContract.DATE));
			float cost = cursor.getFloat(cursor.getColumnIndex(EventContract.COST));
			PersonalDebt pd = new PersonalDebt(new DatabaseHandler(getApplicationContext()));
			cursor = pd.readByEventId(mEventId);
			long[] buddies = null;
			if (cursor != null && cursor.moveToFirst()) {
				buddies = new long[cursor.getCount()];
				int i = 0;
				for ( ; !cursor.isAfterLast(); cursor.moveToNext()) {
					buddies[i] = cursor.getLong(cursor.getColumnIndex(PersonalDebtContract.BUDDY_ID));
					i++;
				}
			}
			displayEventData(title, date, cost, buddies);
		}
	}
	
	private void displayEventData(String title, String date, float cost, long[] buddies) {
		mTitle = title;
		mDate = date;
		mCost = cost;
		
		((TextView)findViewById(R.id.title_info_content)).setText(mTitle);
		((TextView)findViewById(R.id.date_info_content)).setText(mDate);
		((TextView)findViewById(R.id.cost_info_content)).setText(Float.toString(mCost));
		displaySelectedBuddies(buddies);
	}
	
	private void displaySelectedBuddies(long[] selectedBuddies) {
		mBuddyIdList = selectedBuddies;
		if (mBuddyIdList == null || mBuddyIdList.length < 1) {
			TextView tvBuddiesList = (TextView) findViewById(R.id.buddies_info_content);
			tvBuddiesList.setText(getString(R.string.default_buddies_content));
		} else {
			StringBuilder sb = new StringBuilder();
			Buddy buddy = new Buddy(new DatabaseHandler(getApplicationContext()));
			for (long id : mBuddyIdList) {
				Cursor cursor = buddy.read(id);
				if (cursor != null && cursor.moveToFirst()) {
					String name = cursor.getString(cursor.getColumnIndex(BuddyContract.NAME));
					sb.append(name + "; ");
				}
			}

			TextView tvBuddiesList = (TextView) findViewById(R.id.buddies_info_content);
			tvBuddiesList.setText(sb.toString());
		}
	}
	
	private void insertChangesToDb() {		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		Event event = new Event(db);
		long eventId = event.insert(mTitle, mDate, mCost);
		if (eventId != -1 && mBuddyIdList != null && mBuddyIdList.length > 0) {
			float buddyDebt = mCost / mBuddyIdList.length;
			PersonalDebt personalDebt = new PersonalDebt(db);
			for (long buddyId : mBuddyIdList) {
				personalDebt.insert(eventId, buddyId, buddyDebt);
			}
		}
	}
}

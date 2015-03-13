package iradetskyi.app.debt.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.widget.Toast;
import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy;
import iradetskyi.app.debt.data.DatabaseHandler;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import iradetskyi.app.debt.data.Event;
import iradetskyi.app.debt.data.Event.EventContract;
import iradetskyi.app.debt.data.PersonalDebt;
import iradetskyi.app.debt.data.PersonalDebt.PersonalDebtContract;
import iradetskyi.app.debt.utils.DateUtil;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.InputType;
import android.util.Log;
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
	private DateUtil mDateUtil;
	private float mCost;
	private long[] mBuddyIdList;
	private long mEventId = -1;
	private long[] mPaidBuddyList;
	private float[] mPaidBuddyPaymentList;
	
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
		
		mPaidBuddyList = savedInstanceState.getLongArray(SelectBuddyActivity.CREDITORS_EXTRA);
		mPaidBuddyPaymentList = savedInstanceState.getFloatArray(SelectBuddyActivity.PAYMENTS_EXTRA);
		
		displayEventData(
				savedInstanceState.getString(TITLE_EXTRA), 
				savedInstanceState.getString(DATE_EXTRA), 
				savedInstanceState.getFloat(COST_EXTRA), 
				savedInstanceState.getLongArray(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA));
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLongArray(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA, mBuddyIdList);
		outState.putLongArray(SelectBuddyActivity.CREDITORS_EXTRA, mPaidBuddyList);
		outState.putString(TITLE_EXTRA, mTitle);
		outState.putString(DATE_EXTRA, mDateUtil.toString());
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
			showConfirmationDialog();
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
		entry.setText(mTitle);
		builder.setView(entry).setPositiveButton(R.string.dialog_common_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface iface, int id) {
				mTitle = entry.getText().toString();
				((TextView)findViewById(R.id.title_info_content)).setText(mTitle);
				
			}
		});
		builder.create().show();
	}
	
	public void startDatePickerDialog(View v) {
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
				mDateUtil.day = dayOfMonth;
				mDateUtil.month = monthOfYear;
				mDateUtil.year = year;
				((TextView)findViewById(R.id.date_info_content)).setText(mDateUtil.toString());
			}
		}, mDateUtil.year, mDateUtil.month, mDateUtil.day);
		dialog.getDatePicker().setCalendarViewShown(false);
		dialog.show();
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
				mBuddyIdList = data.getLongArrayExtra(SelectBuddyActivity.SELECTED_BUDDIES_EXTRA);
				mPaidBuddyList = data.getLongArrayExtra(SelectBuddyActivity.CREDITORS_EXTRA);
				mPaidBuddyPaymentList = data.getFloatArrayExtra(SelectBuddyActivity.PAYMENTS_EXTRA);
				
				displaySelectedBuddies();
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
		mDateUtil = DateUtil.getCurrentDate();
		displayEventData(
				getString(R.string.activity_event_item_default_title, mDateUtil.toString()), 
				mDateUtil.toString(), 
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
			findViewById(R.id.buddies_info).setClickable(false);
		}
	}

	private void transformForEdit() {
		mAction = EDIT_EVENT_ACTION;
		
		mMenu.setGroupVisible(R.id.menu_group_event_edit, true);
		mMenu.setGroupVisible(R.id.menu_group_event_view, false);
		
		findViewById(R.id.title_info).setClickable(true);
		findViewById(R.id.date_info).setClickable(true);
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
			long[] buddies = getBuddyListFromEvent();
			displayEventData(title, date, cost, buddies);
		}
	}

	private long[] getBuddyListFromEvent() {
		PersonalDebt pd = new PersonalDebt(new DatabaseHandler(getApplicationContext()));
		Cursor cursor = pd.readByEventId(mEventId);
		Set<Long> buddySet = new HashSet<Long>();
		if (cursor != null && cursor.moveToFirst()) {
			long debtor = cursor.getLong(cursor.getColumnIndex(PersonalDebtContract.BUDDY_ID));
			long creditor = cursor.getLong(cursor.getColumnIndex(PersonalDebtContract.CREDITOR_ID));
			buddySet.add(debtor);
			buddySet.add(creditor);
		}
		long[] result = new long[buddySet.size()];
		int i = 0;
		for (long item : buddySet) {
			result[i++] = item;
		}
		return result;
	}

	private void displayEventData(String title, String date, float cost, long[] buddies) {
		mTitle = title;
		mDateUtil = DateUtil.parse(date);
		mCost = cost;
		mBuddyIdList = buddies;
		
		((TextView)findViewById(R.id.title_info_content)).setText(mTitle);
		((TextView)findViewById(R.id.date_info_content)).setText(mDateUtil.toString());
		((TextView)findViewById(R.id.cost_info_content)).setText(Float.toString(mCost));
		displaySelectedBuddies();
	}
	
	private void displaySelectedBuddies() {
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

    private void showConfirmationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.dialog_common_save_changes_title)
                .setMessage(R.string.dialog_common_save_changes_message)
                .setPositiveButton(R.string.dialog_common_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insertChangesToDb();
                                setResult(RESULT_OK);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.dialog_common_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.dialog_common_save_changes_on_cancel,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

	private void insertChangesToDb() {		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		Event event = new Event(db);
		if (mEventId == -1) {
			mEventId = event.insert(mTitle, mDateUtil.toString(), mCost);
		} else {
			event.updateTitle(mEventId, mTitle);
			event.updateDate(mEventId, mDateUtil.toString());
		}
		if (mEventId != -1 && mBuddyIdList != null && mPaidBuddyList != null && mBuddyIdList.length > 0) {
			PersonalDebt personalDebt = new PersonalDebt(db);
			float fullPayment = getFullPayment();
			float fullPartPayment = (float) (fullPayment / mBuddyIdList.length);
			float fullDebt = 0;
			float fullCredit = 0;
			
			List<Long> debtorList = new ArrayList<Long>();
			List<Float> debtList = new ArrayList<Float>();
			List<Long> creditorList = new ArrayList<Long>();
			List<Float> creditList = new ArrayList<Float>();
			
			for (int i = 0; i < mBuddyIdList.length; i++) {
				float paymnet = fullPartPayment;
				for (int j = 0; j < mPaidBuddyList.length; j++) {
					if (mPaidBuddyList[j] == mBuddyIdList[i]) {
						paymnet -= mPaidBuddyPaymentList[j];
					}
				}
				if (paymnet > 0) {
					debtorList.add(mBuddyIdList[i]);
					debtList.add(paymnet);
					fullDebt += paymnet;
				} else {
					creditorList.add(mBuddyIdList[i]);
					creditList.add(-paymnet);
					fullCredit -= paymnet;
				}
			}
			
			if (fullDebt != fullCredit) {
				Log.e("eerror", "fullDebt != fullCredit");
			}
			
			for (int i = 0; i < debtorList.size(); i++) {
				for (int j = 0; j < creditorList.size(); j++) {
					personalDebt.insert(mEventId, debtorList.get(i), creditorList.get(j), creditList.get(j) * debtList.get(i) / fullDebt);
				}
			}
		}
	}
	
	private float getFullPayment() {
		float fullPayment = 0.0f;
		for (float payment : mPaidBuddyPaymentList) {
			fullPayment += payment;
		}
		return fullPayment;
	}
}

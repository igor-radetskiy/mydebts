package iradetskyi.app.debt.list.adapter;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.data.Buddy.BuddyContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SelectBuddyAdapter extends SelectionCursorAdapter {
	private static final int sLayoutId = R.layout.select_buddy_item;
	private static final int sCheckBoxId = R.id.select_buddy_item_checkbox;
	private static final String[] sFrom = {BuddyContract.NAME};
	private static final int[] sTo = {R.id.select_buddy_item_tv_name};

	private Activity mActivity;
	private SparseArray<Float> mPaymentArray = new SparseArray<Float>();
	private float mFullPayment;

	public SelectBuddyAdapter(Activity activity, Cursor c) {
		super(activity, sLayoutId, c, sFrom, sTo, 0, sCheckBoxId);
		mActivity = activity;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Button payBtn = (Button)view.findViewById(R.id.select_buddy_item_btn_pay);
		int userId = cursor.getInt(cursor.getColumnIndex(BuddyContract.ID));
		payBtn.setEnabled(isItemSelected(userId));
		payBtn.setOnClickListener(new OnPayButtonClickListener(userId));

		super.bindView(view, context, cursor);
	}

	public SparseArray<Float> getPayments() {
		return mPaymentArray;
	}

	public float getFullPayment() {
		return mFullPayment;
	}

	private class OnPayButtonClickListener implements View.OnClickListener {
		private long mUserId;

		public OnPayButtonClickListener(long userId) {
			mUserId = userId;
		}

		@Override
		public void onClick(View v) {
			startPaymentDialog();
		}

		private void startPaymentDialog() {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			final View view = inflater.inflate(R.layout.dialog_price_input, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder
				.setTitle(R.string.dialog_price_input_title)
				.setView(view)
				.setPositiveButton(
						R.string.dialog_common_ok, 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText et = (EditText)view.findViewById(R.id.dialog_price_input_et_price);
								float prevPayment = mPaymentArray.get((int)mUserId, 0.0f);
								float payment = Float.parseFloat(et.getText().toString());
								mFullPayment += payment - prevPayment;
								mPaymentArray.put((int)mUserId, payment);
							}
						})
				.create().show();
		}
	}
}

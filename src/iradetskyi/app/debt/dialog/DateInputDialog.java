package iradetskyi.app.debt.dialog;

import iradetskyi.app.debt.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;

public class DateInputDialog {
	private AlertDialog.Builder mBuilder;
	private DatePicker mDatePicker;
	
	public DateInputDialog(Activity activity) {
		mDatePicker = new DatePicker(activity);
		mBuilder = new AlertDialog.Builder(activity);
		mBuilder.setTitle("Set title");
		mBuilder.setView(mDatePicker);
		mBuilder.setPositiveButton(R.string.dialog_common_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
	}
	
	public void show() {
		mBuilder.create().show();
	}
}

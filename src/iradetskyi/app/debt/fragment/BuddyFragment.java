package iradetskyi.app.debt.fragment;

import iradetskyi.app.debt.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BuddyFragment extends Fragment {
	
	public static final String BUDDY_ID_EXTRA = "iradetskyi.app.debt.extra.BUDDY_ID_EXTRA";
	
	private int mBuddyId;
	private String mBuddyName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			mBuddyId = args.getInt(BUDDY_ID_EXTRA);
		}
		
		View view = inflater.inflate(R.layout.fragment_buddy, container);
		return view;
	}
}

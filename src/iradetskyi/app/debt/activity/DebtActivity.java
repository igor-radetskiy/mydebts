package iradetskyi.app.debt.activity;

import iradetskyi.app.debt.R;
import iradetskyi.app.debt.fragment.BuddyFragment;
import iradetskyi.app.debt.fragment.BuddyListFragment;
import iradetskyi.app.debt.fragment.EventListFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DebtActivity extends Activity {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
          .replace(R.id.content_frame, new EventListFragment())
          .commit();

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new DebtNavigationAdapter(this));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.cost_info_title) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onOptionsItemSelected(item);
    }
    
    private class DebtNavigationAdapter extends BaseAdapter {
    	private static final int ITEM_COUNT = 3;
    	private final Fragment[] mFragments = {
    		new BuddyFragment(), //my page
    		new EventListFragment(), // list of events
    		new BuddyListFragment() //list of buddies
    	};
    	private final int[] mDrawables = {
    		R.drawable.ic_action_person,
    		R.drawable.ic_action_event,
    		R.drawable.ic_action_group
    	};
    	private final String[] mDescriptions;
    	private Context mContext;
    	
    	public DebtNavigationAdapter(Context context) {
    		mContext = context;
    		mDescriptions = mContext.getResources().getStringArray(R.array.debt_navigation_items);
    	}

		@Override
		public int getCount() {
			return ITEM_COUNT;
		}

		@Override
		public Object getItem(int position) {
			if (position >= 0 && position < ITEM_COUNT) {
				return mFragments[position];
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup root) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.debt_navigation_item, null);
				TextView tvItem = (TextView) view.findViewById(R.id.tv_debt_navigation_item);
				if (position >= 0 && position < ITEM_COUNT) {
					tvItem.setText(mDescriptions[position]);
					tvItem.setCompoundDrawablesWithIntrinsicBounds(mDrawables[position], 0, 0, 0);
				}
			}
			return view;
		}
    }
}

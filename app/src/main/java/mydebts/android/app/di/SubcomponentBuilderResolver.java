package mydebts.android.app.di;

import android.app.Activity;

import mydebts.android.app.MyDebtsApplication;

public class SubcomponentBuilderResolver {
    public static <T extends Activity> SubcomponentBuilder resolve(T activity) {
        return ((MyDebtsApplication)activity.getApplication())
                .subcomponentBuilder(activity.getClass());
    }
}

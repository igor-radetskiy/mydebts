package mydebts.android.app.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;

import mydebts.android.app.MyDebtsApplication;

public class SubcomponentBuilderResolver {

    public static SubcomponentBuilder resolve(@NonNull Fragment fragment) {
        return resolve(fragment.getActivity(), fragment.getClass());
    }

    public static SubcomponentBuilder resolve(@NonNull Activity activity) {
        return resolve(activity, activity.getClass());
    }

    private static SubcomponentBuilder resolve(@NonNull Activity activity, Class keyClass) {
        return getApplication(activity)
                .subcomponentBuilder(keyClass);
    }

    private static MyDebtsApplication getApplication(@NonNull Activity activity) {
        return (MyDebtsApplication)activity.getApplication();
    }
}

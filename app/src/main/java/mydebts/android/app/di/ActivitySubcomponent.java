package mydebts.android.app.di;

import android.app.Activity;

public interface ActivitySubcomponent<T extends Activity> {
    void inject(T activity);
}

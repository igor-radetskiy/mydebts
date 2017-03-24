package mydebts.android.app.di;

import android.app.Activity;

public interface SubcomponentBuilder<T extends Activity> {

    SubcomponentBuilder<T> module(ActivityModule<T> module);

    ActivitySubcomponent<T> build();
}
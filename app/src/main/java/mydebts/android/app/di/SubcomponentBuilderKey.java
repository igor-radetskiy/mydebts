package mydebts.android.app.di;

import dagger.MapKey;

@MapKey
public @interface SubcomponentBuilderKey {
    Class<?> value();
}

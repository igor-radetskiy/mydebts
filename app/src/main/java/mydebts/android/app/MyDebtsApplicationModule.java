package mydebts.android.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class MyDebtsApplicationModule {
    private final Context context;

    MyDebtsApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideApplicationContext() {
        return context;
    }
}

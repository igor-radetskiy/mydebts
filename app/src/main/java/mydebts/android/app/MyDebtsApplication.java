package mydebts.android.app;

import android.app.Activity;
import android.app.Application;

import java.util.Map;

import javax.inject.Inject;

import mydebts.android.app.di.ActivitySubcomponent;

public class MyDebtsApplication extends Application {

    @Inject Map<Class<? extends Activity>, ActivitySubcomponent.Builder> subcomponentBuilders;

    @Override
    public void onCreate() {
        super.onCreate();

       DaggerMyDebtsApplicationComponent.builder()
                .myDebtsApplicationModule(new MyDebtsApplicationModule(this))
                .build().inject(this);
    }

    public <T extends Activity> ActivitySubcomponent.Builder subcomponentBuilder(Class<T> tClass) {
        return subcomponentBuilders.get(tClass);
    }
}

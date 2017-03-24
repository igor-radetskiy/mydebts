package mydebts.android.app;

import android.app.Activity;
import android.app.Application;

import java.util.Map;

import javax.inject.Inject;

import mydebts.android.app.di.SubcomponentBuilder;

public class MyDebtsApplication extends Application {

    @Inject Map<Class<? extends Activity>, SubcomponentBuilder<? extends Activity>> subcomponentBuilders;

    @Override
    public void onCreate() {
        super.onCreate();

       DaggerMyDebtsApplicationComponent.builder()
                .myDebtsApplicationModule(new MyDebtsApplicationModule(this))
                .build().inject(this);
    }

    public <T extends Activity> SubcomponentBuilder<T> subcomponentBuilder(Class<T> tClass) {
        return (SubcomponentBuilder<T>) subcomponentBuilders.get(tClass);
    }
}

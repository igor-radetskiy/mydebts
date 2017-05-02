package mydebts.android.app;

import android.app.Application;

import java.util.Map;

import javax.inject.Inject;

import mydebts.android.app.di.SubcomponentBuilder;

public class MyDebtsApplication extends Application {

    @Inject Map<Class<?>, SubcomponentBuilder> subcomponentBuilders;

    @Override
    public void onCreate() {
        super.onCreate();

       DaggerMyDebtsApplicationComponent.builder()
                .myDebtsApplicationModule(new MyDebtsApplicationModule(this))
                .build().inject(this);
    }

    public SubcomponentBuilder subcomponentBuilder(Class<?> keyClass) {
        return subcomponentBuilders.get(keyClass);
    }
}

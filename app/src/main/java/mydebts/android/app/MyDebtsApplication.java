package mydebts.android.app;

import android.app.Application;

public class MyDebtsApplication extends Application {
    private MyDebtsApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyDebtsApplicationComponent.builder()
                .myDebtsApplicationModule(new MyDebtsApplicationModule(this))
                .build();
    }

    public MyDebtsApplicationComponent getComponent() {
        return component;
    }
}

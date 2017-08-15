package mydebts.android.app;

import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;
import mydebts.android.app.data.DataModule;
import mydebts.android.app.res.ResourcesModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = { DataModule.class, ResourcesModule.class, MyDebtsBindingModule.class } )
interface MyDebtsComponent {

    void inject(MyDebtsApplication application);

    @Component.Builder
    interface Builder {

        MyDebtsComponent build();

        @BindsInstance Builder applicationContext(Context context);
    }
}

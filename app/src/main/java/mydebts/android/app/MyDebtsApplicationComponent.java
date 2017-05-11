package mydebts.android.app;

import javax.inject.Singleton;

import dagger.Component;
import mydebts.android.app.data.DataModule;
import mydebts.android.app.data.db.DbModule;

@Singleton
@Component(modules = {
        MyDebtsApplicationModule.class,
        DataModule.class,
        MyDebtsApplicationBindingModule.class})
interface MyDebtsApplicationComponent {

    void inject(MyDebtsApplication application);
}

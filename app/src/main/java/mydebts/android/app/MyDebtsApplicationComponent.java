package mydebts.android.app;

import javax.inject.Singleton;

import dagger.Component;
import mydebts.android.app.db.DbModule;

@Singleton
@Component(modules = {
        MyDebtsApplicationModule.class,
        DbModule.class,
        MyDebtsApplicationBindingModule.class})
interface MyDebtsApplicationComponent {

    void inject(MyDebtsApplication application);
}

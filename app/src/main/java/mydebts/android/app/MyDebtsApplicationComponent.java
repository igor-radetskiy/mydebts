package mydebts.android.app;

import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import mydebts.android.app.db.DaoSession;
import mydebts.android.app.db.DbModule;

@Singleton
@Component(modules = {
        MyDebtsApplicationModule.class,
        DbModule.class
})
public interface MyDebtsApplicationComponent {

    DaoSession daoSession();
}

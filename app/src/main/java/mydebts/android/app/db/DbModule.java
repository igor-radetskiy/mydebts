package mydebts.android.app.db;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mydebts.android.app.R;

@Module
public class DbModule {
    @Provides
    @Singleton
    DaoSession provideDaoSession(Context context) {
        DaoMaster.DevOpenHelper helper =
                new DaoMaster.DevOpenHelper(context, context.getString(R.string.db_name), null);

        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

        return daoMaster.newSession();
    }
}

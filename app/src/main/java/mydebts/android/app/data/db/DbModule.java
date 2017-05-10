package mydebts.android.app.data.db;

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

    @Provides
    EventDao provideEventDao(DaoSession daoSession) {
        return daoSession.getEventDao();
    }

    @Provides
    PersonDao providePersonDao(DaoSession daoSession) {
        return daoSession.getPersonDao();
    }

    @Provides
    ParticipantDao provideParticipantDao(DaoSession daoSession) {
        return daoSession.getParticipantDao();
    }
}

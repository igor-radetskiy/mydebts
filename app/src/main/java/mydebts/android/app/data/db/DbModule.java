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
    EventsTableDao provideEventDao(DaoSession daoSession) {
        return daoSession.getEventsTableDao();
    }

    @Provides
    PersonsTableDao providePersonDao(DaoSession daoSession) {
        return daoSession.getPersonsTableDao();
    }

    @Provides
    ParticipantsTableDao provideParticipantDao(DaoSession daoSession) {
        return daoSession.getParticipantsTableDao();
    }
}

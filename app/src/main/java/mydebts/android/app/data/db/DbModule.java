package mydebts.android.app.data.db;

import android.database.sqlite.SQLiteDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Provides
    SQLiteDatabase provideSqliteDatabase(MyDebtsDbOpenHelper dbOpenHelper) {
        return dbOpenHelper.getWritableDatabase();
    }
}

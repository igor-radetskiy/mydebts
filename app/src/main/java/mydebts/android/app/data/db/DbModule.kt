package mydebts.android.app.data.db

import android.database.sqlite.SQLiteDatabase

import dagger.Module
import dagger.Provides

@Module
class DbModule {
    @Provides internal fun provideSqliteDatabase(dbOpenHelper: MyDebtsDbOpenHelper): SQLiteDatabase
            = dbOpenHelper.writableDatabase
}

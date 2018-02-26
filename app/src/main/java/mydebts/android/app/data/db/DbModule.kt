package mydebts.android.app.data.db

import android.database.sqlite.SQLiteDatabase

import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    internal fun provideSQLiteDatabase(dbOpenHelper: MyDebtsDbOpenHelper): SQLiteDatabase
            = dbOpenHelper.writableDatabase
}

package mydebts.android.app.data.db;

import android.provider.BaseColumns;

public interface EventContract extends BaseColumns {
    String TABLE_NAME = "Events";
    String COLUMN_NAME = "name";
    String COLUMN_DATE = "date";
}

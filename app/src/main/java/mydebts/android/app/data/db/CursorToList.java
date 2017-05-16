package mydebts.android.app.data.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public class CursorToList<T> implements Function<Cursor, List<T>> {
    private final Function<Cursor, T> itemMapper;

    public CursorToList(Function<Cursor, T> itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public List<T> apply(Cursor cursor) throws Exception {
        List<T> ts = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ts.add(itemMapper.apply(cursor));
        }

        cursor.close();

        return ts;
    }
}

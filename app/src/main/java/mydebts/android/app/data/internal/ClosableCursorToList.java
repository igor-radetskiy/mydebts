package mydebts.android.app.data.internal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

class ClosableCursorToList<T> implements Function<Cursor, List<T>> {
    private final Function<Cursor, T> itemMapper;

    ClosableCursorToList(Function<Cursor, T> itemMapper) {
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

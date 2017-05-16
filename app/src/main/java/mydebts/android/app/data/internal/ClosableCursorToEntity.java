package mydebts.android.app.data.internal;

import android.database.Cursor;

import io.reactivex.functions.Function;

class ClosableCursorToEntity<T> implements Function<Cursor, T> {
    private final Function<Cursor, T> mapper;

    ClosableCursorToEntity(Function<Cursor, T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T apply(Cursor cursor) throws Exception {
        T t = mapper.apply(cursor);
        cursor.close();
        return t;
    }
}

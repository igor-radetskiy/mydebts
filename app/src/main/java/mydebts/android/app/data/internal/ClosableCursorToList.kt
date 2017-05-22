package mydebts.android.app.data.internal

import android.database.Cursor

import java.util.ArrayList

import io.reactivex.functions.Function

internal class ClosableCursorToList<T>(private val itemMapper: Function<Cursor, T>) : Function<Cursor, List<T>> {

    @Throws(Exception::class)
    override fun apply(cursor: Cursor): List<T> {
        val ts = ArrayList<T>()

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            ts.add(itemMapper.apply(cursor))
            cursor.moveToNext()
        }

        cursor.close()

        return ts
    }
}

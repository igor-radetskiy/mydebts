package mydebts.android.app.data.internal

import android.database.Cursor

import io.reactivex.functions.Function

internal class ClosableCursorToEntity<T>(private val mapper: Function<Cursor, T>) : Function<Cursor, T> {

    @Throws(Exception::class)
    override fun apply(cursor: Cursor): T {
        val t = mapper.apply(cursor)
        cursor.close()
        return t
    }
}

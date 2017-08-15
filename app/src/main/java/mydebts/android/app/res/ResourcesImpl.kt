package mydebts.android.app.res

import android.content.Context
import android.support.annotation.StringRes
import javax.inject.Inject

class ResourcesImpl @Inject constructor(val context: Context): Resources {
    override fun string(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}
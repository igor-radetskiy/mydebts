package mydebts.android.app.res

import android.support.annotation.StringRes

interface Resources {
    fun string(@StringRes resId: Int): String
}
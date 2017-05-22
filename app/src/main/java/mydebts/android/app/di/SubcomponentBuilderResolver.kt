package mydebts.android.app.di

import android.app.Activity
import android.support.v4.app.Fragment

import mydebts.android.app.MyDebtsApplication

object SubcomponentBuilderResolver {

    fun resolve(fragment: Fragment): SubcomponentBuilder {
        return resolve(fragment.activity, fragment.javaClass)
    }

    fun resolve(activity: Activity): SubcomponentBuilder {
        return resolve(activity, activity.javaClass)
    }

    private fun resolve(activity: Activity, keyClass: Class<*>): SubcomponentBuilder {
        return getApplication(activity)
                .subcomponentBuilder(keyClass)
    }

    private fun getApplication(activity: Activity): MyDebtsApplication {
        return activity.application as MyDebtsApplication
    }
}

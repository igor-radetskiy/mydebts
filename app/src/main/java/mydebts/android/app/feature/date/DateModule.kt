package mydebts.android.app.feature.date

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module
class DateModule {

    @Provides
    @Date
    @Singleton
    internal fun provideDateSubject(): PublishSubject<Triple<Int, Int, Int>> =
            PublishSubject.create()

    @Provides
    @Date
    @Singleton
    internal fun provideDateObservable(@Date publishSubject: PublishSubject<Triple<Int, Int, Int>>)
            : Observable<Triple<Int, Int, Int>> = publishSubject
}
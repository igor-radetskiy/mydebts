package mydebts.android.app.feature.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import mydebts.android.app.data.Delete
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.Insert
import mydebts.android.app.data.Update
import mydebts.android.app.data.model.Event
import mydebts.android.app.rx.RxUtil
import java.util.Collections
import javax.inject.Inject

class EventsViewModel constructor(
        private val eventsSource: EventsSource,
        private val insertEventObservable: Observable<Event>,
        private val updateEventObservable: Observable<Event>,
        private val deleteEventObservable: Observable<Event>,
        private val rxUtil: RxUtil): ViewModel() {

    private val eventsList = ArrayList<Event>()
    private val disposables = CompositeDisposable()

    private var addEventDisposable: Disposable? = null
    private var updateEventDisposable: Disposable? = null
    private var deleteEventDisposable: Disposable? = null

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() {
            loadEvents()
            return _events
        }

    private val _addEventNavigation = MutableLiveData<Boolean?>()
    val addEventNavigation: LiveData<Boolean?>
        get() = _addEventNavigation

    private var _eventNavigation = MutableLiveData<Event?>()
    val eventNavigation: LiveData<Event?>
        get() = _eventNavigation

    override fun onCleared() {
        disposables.clear()
        addEventDisposable?.dispose()
        updateEventDisposable?.dispose()
        deleteEventDisposable?.dispose()
    }

    fun onAddEventClick() {
        if (addEventDisposable == null) {
            addEventDisposable = insertEventObservable
                    .compose(rxUtil.observableSchedulersTransformer())
                    .subscribe {
                        eventsList.add(it)
                        Collections.sort(eventsList, EventComparator())
                        _events.value = eventsList
                    }
        }
        _addEventNavigation.value = true
        _addEventNavigation.value = null
    }

    fun onEventClick(position: Int) {
        if (updateEventDisposable == null) {
            updateEventDisposable = updateEventObservable
                    .compose(rxUtil.observableSchedulersTransformer())
                    .subscribe { result ->
                        eventsList.indexOfFirst { it.id == result.id }
                                .takeIf { it >= 0 && result.date?.time != eventsList[it].date?.time }
                                ?.let {
                                    eventsList[it] = result
                                    _events.value = eventsList
                                }
                    }
        }

        if (deleteEventDisposable == null) {
            deleteEventDisposable = deleteEventObservable
                    .compose(rxUtil.observableSchedulersTransformer())
                    .subscribe {result ->
                        eventsList.indexOfFirst { it.id == result.id }
                                .takeIf { it >= 0 }
                                ?.let {
                                    eventsList.removeAt(it)
                                    _events.value = eventsList
                                }
                    }
        }

        _eventNavigation.value = eventsList[position]
        _eventNavigation.value = null
    }

    private fun loadEvents() {
        disposables.add(eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe { result ->
                    eventsList.clear()
                    eventsList.addAll(result)
                    Collections.sort(eventsList, EventComparator())
                    _events.value = eventsList
                })
    }

    class Factory @Inject constructor(
            private val eventsSource: EventsSource,
            @Insert private val insertEventObservable: Observable<Event>,
            @Update private val updateEventObservable: Observable<Event>,
            @Delete private val deleteEventObservable: Observable<Event>,
            private val rxUtil: RxUtil) : ViewModelProvider.Factory
    {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                EventsViewModel(
                        eventsSource,
                        insertEventObservable,
                        updateEventObservable,
                        deleteEventObservable,
                        rxUtil) as T
    }
}
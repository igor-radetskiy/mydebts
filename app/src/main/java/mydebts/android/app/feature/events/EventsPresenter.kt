package mydebts.android.app.feature.events

import android.view.View
import io.reactivex.disposables.CompositeDisposable
import mydebts.android.app.data.EventsSource
import mydebts.android.app.data.model.Event
import mydebts.android.app.rx.RxUtil
import javax.inject.Inject

class EventsPresenter @Inject constructor(
        private val screen: EventsScreen,
        private val eventsSource: EventsSource,
        private val rxUtil: RxUtil) {

    private val disposables = CompositeDisposable()

    private val events = ArrayList<Event>()
    private val selections = ArrayList<Int>()

    fun getEvents(): List<Event> = events

    fun getSelections(): List<Int> = selections

    fun onViewCreated() {
        disposables.add(eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .doOnSubscribe { events.clear() }
                .doOnSuccess { events.addAll(it) }
                .subscribe { _ -> handleEvents() })
    }

    fun onDestroyView() {
        disposables.clear()
    }

    fun onEventClick(position: Int) {
        screen.showEvent(events[position])
    }

    fun onSelectEvent(position: Int) {
        if (selections.contains(position)) {
            selections.remove(position)
        } else {
            selections.add(position)
        }

        screen.notifyItemChanged(position)
    }

    fun clearSelections() {
        selections.forEach { screen.notifyItemChanged(it) }
        selections.clear()
    }

    fun deleteSelectedEvents() {
        eventsSource.delete(selections.map { events[it] })
                .compose(rxUtil.singleSchedulersTransformer())
                .toObservable()
                .flatMapIterable { it }
                .map { events.indexOf(it) }
                .subscribe { position -> events.removeAt(position); screen.notifyItemRemoved(position) }
    }

    private fun handleEvents() {
        if (events.isNotEmpty()) {
            screen.setEventsVisibility(View.VISIBLE)
            screen.setEmptyViewVisibility(View.GONE)
            screen.notifyDataSetChanged()
        } else {
            screen.setEventsVisibility(View.GONE)
            screen.setEmptyViewVisibility(View.VISIBLE)
        }
    }
}
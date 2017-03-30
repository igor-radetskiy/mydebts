package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import mydebts.android.app.db.Event;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.rx.RxUtil;

@ActivityScope
class EventsViewModel {

    private final EventsSource eventsSource;
    private final RxUtil rxUtil;
    private final BehaviorSubject<List<Event>> eventsSubject = BehaviorSubject.create();

    @Inject
    EventsViewModel(EventsSource eventsSource, RxUtil rxUtil) {
        this.eventsSource = eventsSource;
        this.rxUtil = rxUtil;
        fetchEvents();
    }

    Observable<List<Event>> eventsSubject() {
        return eventsSubject;
    }

    private void fetchEvents() {
        eventsSource.fetch()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe(this::setEvents, this::setError);
    }

    private void setEvents(List<Event> events) {
        eventsSubject.onNext(events);
    }

    private void setError(Throwable throwable) {

    }
}

package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.data.EventsSource;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.di.SingleIn;
import mydebts.android.app.rx.RxUtil;

@SingleIn(EventsFragment.class)
class EventsViewModel {

    private final EventsSource eventsSource;
    private final RxUtil rxUtil;

    @Inject
    EventsViewModel(EventsSource eventsSource, RxUtil rxUtil) {
        this.eventsSource = eventsSource;
        this.rxUtil = rxUtil;
    }

    Single<List<Event>> fetchEvents() {
        return eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer());
    }
}

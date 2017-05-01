package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.db.Event;
import mydebts.android.app.di.ActivityScope;
import mydebts.android.app.rx.RxUtil;

@ActivityScope
class EventsViewModel {

    private final EventsSource eventsSource;
    private final RxUtil rxUtil;

    @Inject
    EventsViewModel(EventsSource eventsSource, RxUtil rxUtil) {
        this.eventsSource = eventsSource;
        this.rxUtil = rxUtil;
    }

    Single<List<Event>> fetchEvents() {
        return eventsSource.fetch()
                .compose(rxUtil.singleSchedulersTransformer());
    }
}

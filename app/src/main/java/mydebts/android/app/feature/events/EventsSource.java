package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.db.Event;
import mydebts.android.app.db.EventDao;

class EventsSource {
    private final EventDao dao;

    @Inject
    EventsSource(EventDao dao) {
        this.dao = dao;
    }

    Single<List<Event>> fetch() {
        return Single.fromCallable(dao::loadAll);
    }
}

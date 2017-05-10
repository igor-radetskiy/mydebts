package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.data.db.EventsTable;
import mydebts.android.app.data.db.EventDao;

class EventsSource {
    private final EventDao dao;

    @Inject
    EventsSource(EventDao dao) {
        this.dao = dao;
    }

    Single<List<EventsTable>> fetch() {
        return Single.fromCallable(dao::loadAll);
    }
}

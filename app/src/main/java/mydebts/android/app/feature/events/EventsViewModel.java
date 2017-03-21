package mydebts.android.app.feature.events;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import mydebts.android.app.db.Event;
import mydebts.android.app.db.EventDao;
import mydebts.android.app.di.ActivityScope;

@ActivityScope
class EventsViewModel {
    private final EventDao dao;
    private final BehaviorSubject<List<Event>> eventsSubject = BehaviorSubject.create();

    @Inject
    EventsViewModel(EventDao dao) {
        this.dao = dao;
    }

    Observable<List<Event>> eventsSubject() {
        return eventsSubject;
    }
}

package mydebts.android.app.feature.main;

import mydebts.android.app.db.Event;

public interface MainRouter {

    void navigateToEvents();

    void navigateToNewEvent();

    void navigateToEvent(Event event);
}

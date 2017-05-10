package mydebts.android.app.feature.main;

import android.support.annotation.NonNull;

import mydebts.android.app.data.db.EventsTable;

public interface MainRouter {

    void navigateToEvents();

    void navigateToNewEvent();

    void navigateToEvent(@NonNull EventsTable event);

    void navigateBack();
}

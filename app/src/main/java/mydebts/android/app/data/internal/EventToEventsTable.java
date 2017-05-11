package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.EventsTable;
import mydebts.android.app.data.model.Event;

class EventToEventsTable implements Function<Event, EventsTable> {
    @Override
    public EventsTable apply(Event event) throws Exception {
        EventsTable eventsTable = new EventsTable();
        eventsTable.setId(event.getId());
        eventsTable.setName(event.getName());
        eventsTable.setDate(event.getDate());
        return eventsTable;
    }
}

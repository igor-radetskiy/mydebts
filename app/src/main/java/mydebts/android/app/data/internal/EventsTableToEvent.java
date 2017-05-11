package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.EventsTable;
import mydebts.android.app.data.model.Event;

class EventsTableToEvent implements Function<EventsTable, Event> {
    @Override
    public Event apply(EventsTable eventsTable) throws Exception {
        return Event.builder()
                .id(eventsTable.getId())
                .name(eventsTable.getName())
                .date(eventsTable.getDate())
                .build();
    }
}

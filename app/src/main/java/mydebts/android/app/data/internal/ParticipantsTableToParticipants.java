package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.ParticipantsTable;
import mydebts.android.app.data.model.Participant;

class ParticipantsTableToParticipants implements Function<ParticipantsTable, Participant> {
    private static final EventsTableToEvent EVENTS_TABLE_MAPPER = new EventsTableToEvent();
    private static final PersonsTableToPerson PERSONS_TABLE_MAPPER = new PersonsTableToPerson();

    @Override
    public Participant apply(ParticipantsTable participantsTable) throws Exception {
        return Participant.builder()
                .id(participantsTable.getId())
                .event(EVENTS_TABLE_MAPPER.apply(participantsTable.getEvent()))
                .person(PERSONS_TABLE_MAPPER.apply(participantsTable.getPerson()))
                .debt(participantsTable.getDebt())
                .build();
    }
}

package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.ParticipantsTable;
import mydebts.android.app.data.model.Participant;

public class ParticipantToParticipantsTable implements Function<Participant, ParticipantsTable> {
    @Override
    public ParticipantsTable apply(Participant participant) throws Exception {
        return new ParticipantsTable(participant.getId(), participant.getEvent().getId(),
                participant.getPerson().getId(), participant.getDebt());
    }
}

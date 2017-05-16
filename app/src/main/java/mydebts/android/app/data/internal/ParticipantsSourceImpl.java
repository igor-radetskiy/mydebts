package mydebts.android.app.data.internal;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.data.ParticipantsSource;
import mydebts.android.app.data.db.ParticipantTable;
import mydebts.android.app.data.model.Participant;

public class ParticipantsSourceImpl implements ParticipantsSource {
    private static final CursorToParticipant CURSOR_TO_PARTICIPANT = new CursorToParticipant();
    private static final ClosableCursorToList<Participant> CLOSABLE_CURSOR_TO_PARTICIPANTS
            = new ClosableCursorToList<>(CURSOR_TO_PARTICIPANT);

    private final ParticipantTable participantTable;

    @Inject
    ParticipantsSourceImpl(ParticipantTable participantTable) {
        this.participantTable = participantTable;
    }

    @Override
    public Single<List<Participant>> getByEventId(@NonNull Long eventId) {
        return Single.fromCallable(() -> participantTable.queryByEventId(eventId))
                .map(CLOSABLE_CURSOR_TO_PARTICIPANTS);
    }

    @Override
    public Single<List<Participant>> getByPersonId(@NonNull Long personId) {
        return Single.fromCallable(() -> participantTable.queryByPersonId(personId))
                .map(CLOSABLE_CURSOR_TO_PARTICIPANTS);
    }

    @Override
    public Single<Participant> insert(@NonNull Participant participant) {
        return Single.fromCallable(() -> participantTable.insert(participant))
                .map(id -> Participant.builder(participant)
                                .id(id)
                                .build());
    }

    @Override
    public Single<Participant> update(@NonNull Participant participant) {
        return Single.fromCallable(() -> participantTable.update(participant))
                .map(affectedRows -> participant);
    }

    @Override
    public Single<Participant> delete(@NonNull Participant participant) {
        return Single.fromCallable(() -> participantTable.delete(participant))
                .map(affectedRows -> participant);
    }
}

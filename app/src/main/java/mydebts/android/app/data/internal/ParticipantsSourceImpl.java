package mydebts.android.app.data.internal;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import mydebts.android.app.data.ParticipantsSource;
import mydebts.android.app.data.db.ParticipantsTable;
import mydebts.android.app.data.db.ParticipantsTableDao;
import mydebts.android.app.data.model.Participant;

public class ParticipantsSourceImpl implements ParticipantsSource {
    private static final ParticipantToParticipantsTable TO_DB_MAPPER = new ParticipantToParticipantsTable();
    private static final ParticipantsTableToParticipants FROM_DB_MAPPER = new ParticipantsTableToParticipants();
    private static final ListToList<ParticipantsTable, Participant> FROM_DB_LIST_MAPPER
            = new ListToList<>(FROM_DB_MAPPER);

    private final ParticipantsTableDao dao;

    @Inject
    ParticipantsSourceImpl(ParticipantsTableDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<List<Participant>> getByEventId(@NonNull Long eventId) {
        return Single.fromCallable(() -> dao.queryRaw("WHERE " + ParticipantsTableDao.Properties.EventId.columnName + "=?", Long.toString(eventId)))
                .map(FROM_DB_LIST_MAPPER);
    }

    @Override
    public Single<List<Participant>> getByPersonId(@NonNull Long personId) {
        return null;
    }

    @Override
    public Single<Participant> insert(@NonNull Participant participant) {
        return Single.just(participant)
                .map(TO_DB_MAPPER)
                .flatMap(this::insertToDb)
                .map(id -> Participant.builder(participant)
                                .id(id)
                                .build());
    }

    @Override
    public Single<Integer> deleteByEventId(@NonNull Long eventId) {
        return Single.fromCallable(() -> {
            List<ParticipantsTable> participants = dao.queryRaw("WHERE " + ParticipantsTableDao.Properties.EventId.columnName + "=?", Long.toString(eventId));
            for (ParticipantsTable participant : participants) {
                participant.delete();
            }
            return participants.size();
        });
    }

    private Single<Long> insertToDb(ParticipantsTable participantsTable) {
        return Single.fromCallable(() -> dao.insert(participantsTable));
    }
}

package mydebts.android.app.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;
import mydebts.android.app.data.model.Participant;

public interface ParticipantsSource {

    Single<List<Participant>> getByEventId(@NonNull Long eventId);

    Single<List<Participant>> getByPersonId(@NonNull Long personId);

    Single<Participant> insert(@NonNull Participant participant);

    Single<Integer> deleteByEventId(@NonNull Long eventId);
}

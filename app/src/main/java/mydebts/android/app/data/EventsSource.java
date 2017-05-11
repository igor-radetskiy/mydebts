package mydebts.android.app.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import mydebts.android.app.data.model.Event;

public interface EventsSource {

    Single<List<Event>> getAll();

    Single<Event> get(@NonNull Long id);

    Single<Event> insert(@NonNull Event event);

    Single<Event> update(@NonNull Event event);

    Single<Event> delete(@NonNull Event event);

    Observable<Event> inserted();

    Observable<Event> updated();

    Observable<Event> deleted();
}

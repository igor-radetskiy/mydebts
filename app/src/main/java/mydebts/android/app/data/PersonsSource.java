package mydebts.android.app.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import mydebts.android.app.data.model.Person;

public interface PersonsSource {

    Single<List<Person>> getAll();

    Single<Person> get(@NonNull Long id);

    Observable<Person> inserted();

    Observable<Person> updated();

    Observable<Person> deleted();
}

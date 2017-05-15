package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.PersonsTable;
import mydebts.android.app.data.model.Person;

public class PersonToPersonsTable implements Function<Person, PersonsTable> {
    @Override
    public PersonsTable apply(Person person) throws Exception {
        return new PersonsTable(person.getId(), person.getName());
    }
}

package mydebts.android.app.data.internal;

import io.reactivex.functions.Function;
import mydebts.android.app.data.db.PersonsTable;
import mydebts.android.app.data.model.Person;

class PersonsTableToPerson implements Function<PersonsTable, Person> {
    @Override
    public Person apply(PersonsTable personsTable) throws Exception {
        return Person.builder()
                .id(personsTable.getId())
                .name(personsTable.getName())
                .build();
    }
}

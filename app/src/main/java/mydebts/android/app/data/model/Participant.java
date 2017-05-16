package mydebts.android.app.data.model;

import android.support.annotation.NonNull;

public class Participant {
    private Long id;
    private Event event;
    private Person person;
    private Double debt;

    public Participant(Long id, Event event, Person person, Double debt) {
        this.id = id;
        this.event = event;
        this.person = person;
        this.debt = debt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", event=" + event +
                ", person=" + person +
                ", debt=" + debt +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(@NonNull Participant source) {
        return builder()
                .id(source.getId())
                .event(source.getEvent())
                .person(source.getPerson())
                .debt(source.getDebt());
    }

    public static class Builder {
        private Long id;
        private Event event;
        private Person person;
        private Double debt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder event(Event event) {
            this.event = event;
            return this;
        }

        public Builder person(Person person) {
            this.person = person;
            return this;
        }

        public Builder debt(Double debt) {
            this.debt = debt;
            return this;
        }

        public Participant build() {
            return new Participant(id, event, person, debt);
        }
    }
}

package mydebts.android.app.data.model

class Participant(var id: Long?, var event: Event?, var person: Person?, var debt: Double?) {

    override fun toString(): String {
        return "Participant{" +
                "id=" + id +
                ", event=" + event +
                ", person=" + person +
                ", debt=" + debt +
                '}'
    }

    class Builder {
        private var id: Long? = null
        private var event: Event? = null
        private var person: Person? = null
        private var debt: Double? = null

        fun id(id: Long?): Builder {
            this.id = id
            return this
        }

        fun event(event: Event?): Builder {
            this.event = event
            return this
        }

        fun person(person: Person?): Builder {
            this.person = person
            return this
        }

        fun debt(debt: Double?): Builder {
            this.debt = debt
            return this
        }

        fun build(): Participant {
            return Participant(id, event, person, debt)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }

        fun builder(source: Participant): Builder {
            return builder()
                    .id(source.id)
                    .event(source.event)
                    .person(source.person)
                    .debt(source.debt)
        }
    }
}

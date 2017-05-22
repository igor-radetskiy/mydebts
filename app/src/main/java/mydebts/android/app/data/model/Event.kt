package mydebts.android.app.data.model

import java.util.Date

class Event(var id: Long?, var name: String?, var date: Date?) {

    override fun toString(): String {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}'
    }

    class Builder {
        private var id: Long? = null
        private var name: String? = null
        private var date: Date? = null

        fun id(id: Long?): Builder {
            this.id = id
            return this
        }

        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        fun date(date: Date?): Builder {
            this.date = date
            return this
        }

        fun build(): Event {
            return Event(id, name, date)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }

        fun builder(source: Event): Builder {
            return builder()
                    .id(source.id)
                    .name(source.name)
                    .date(source.date)
        }
    }
}

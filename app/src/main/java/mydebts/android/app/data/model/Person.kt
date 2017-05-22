package mydebts.android.app.data.model

class Person(var id: Long?, var name: String?) {

    override fun toString(): String {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }

    class Builder {
        private var id: Long? = null
        private var name: String? = null

        fun id(id: Long?): Builder {
            this.id = id
            return this
        }

        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        fun build(): Person {
            return Person(id, name)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }

        fun builder(source: Person): Builder {
            return builder()
                    .id(source.id)
                    .name(source.name)
        }
    }
}

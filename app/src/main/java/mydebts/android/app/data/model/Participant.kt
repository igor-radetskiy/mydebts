package mydebts.android.app.data.model

data class Participant(
        var id: Long? = null,
        var event: Event? = null,
        var person: Person? = null,
        var debt: Double? = null)

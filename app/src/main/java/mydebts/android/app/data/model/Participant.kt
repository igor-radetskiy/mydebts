package mydebts.android.app.data.model

import android.os.Parcel
import android.os.Parcelable

data class Participant(
        var id: Long? = null,
        var event: Event? = null,
        var person: Person? = null,
        var debt: Double? = null) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Participant> = object : Parcelable.Creator<Participant> {
            override fun createFromParcel(source: Parcel): Participant = Participant(source)
            override fun newArray(size: Int): Array<Participant?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Long::class.java.classLoader) as Long?,
    source.readParcelable<Event>(Event::class.java.classLoader),
    source.readParcelable<Person>(Person::class.java.classLoader),
    source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeParcelable(event, 0)
        dest.writeParcelable(person, 0)
        dest.writeValue(debt)
    }
}

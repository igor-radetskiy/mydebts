package mydebts.android.app.data.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Event(
        var id: Long? = null,
        var name: String? = null,
        var date: Date? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Event> = object : Parcelable.Creator<Event> {
            override fun createFromParcel(source: Parcel): Event = Event(source)
            override fun newArray(size: Int): Array<Event?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Long::class.java.classLoader) as Long?,
    source.readString(),
    source.readSerializable() as Date?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
        dest.writeSerializable(date)
    }
}

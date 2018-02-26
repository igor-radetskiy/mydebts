package mydebts.android.app.data.model

import android.os.Parcel
import android.os.Parcelable

data class Person(
        var id: Long? = null,
        var name: String? = null) : Parcelable {
    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<Person> = object : Parcelable.Creator<Person> {
            override fun createFromParcel(source: Parcel): Person = Person(source)
            override fun newArray(size: Int): Array<Person?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readValue(Long::class.java.classLoader) as Long?,
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
    }
}

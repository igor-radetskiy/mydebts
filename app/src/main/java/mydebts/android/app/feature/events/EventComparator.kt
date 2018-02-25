package mydebts.android.app.feature.events

import mydebts.android.app.data.model.Event
import java.util.Date

class EventComparator: Comparator<Event> {

    override fun compare(e1: Event, e2: Event): Int {
        val date1 = e1.date ?: BEGINNING
        val date2 = e2.date ?: BEGINNING

        return date1.compareTo(date2)
    }

    companion object {
        private val BEGINNING = Date(0)
    }
}
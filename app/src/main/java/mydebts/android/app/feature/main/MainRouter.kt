package mydebts.android.app.feature.main

import mydebts.android.app.data.model.Event
import mydebts.android.app.data.model.Person

interface MainRouter {

    fun navigateToEvents()

    fun navigateToNewEvent()

    fun navigateToEvent(event: Event)

    fun navigateBack()
}

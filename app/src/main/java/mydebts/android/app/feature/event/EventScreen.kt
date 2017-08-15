package mydebts.android.app.feature.event

import mydebts.android.app.data.model.Participant

interface EventScreen {

    fun showTitle(title: CharSequence)

    fun showDatePicker(year: Int, month: Int, day: Int)

    fun showParticipants(participants: List<Participant>)

    fun showAddedParticipantAt(position:Int)

    fun showParticipant(participant: Participant)

    fun showNewParticipant()
}
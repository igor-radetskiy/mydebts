package mydebts.android.app.feature.event

import mydebts.android.app.data.model.Participant

interface EventScreen {

    fun showTitle(title: CharSequence)

    fun setDeleteMenuItemVisible(visible: Boolean)

    fun showDatePicker(year: Int, month: Int, day: Int)

    fun setEmptyViewVisibility(visibility: Int)

    fun setParticipantsViewVisibility(visibility: Int)

    fun notifyParticipantsChanged()

    fun notifyParticipantInserted(position:Int)

    fun notifyParticipantChanged(position:Int)

    fun showParticipant(participant: Participant)

    fun showNewParticipant()

    fun navigateBack()
}
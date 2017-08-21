package mydebts.android.app.feature.participant

import mydebts.android.app.data.model.Participant

interface ParticipantScreen {

    fun showName(name: CharSequence)

    fun showNameError(error: CharSequence?)

    fun setNameEnabled(enabled: Boolean)

    fun requestFocusOnDebtInput()

    fun showDebt(debt: Double)

    fun showDebtError(error: CharSequence?)

    fun setPersonsSuggestions(persons: List<String>)

    fun setResult(result: Participant)

    fun finish()
}
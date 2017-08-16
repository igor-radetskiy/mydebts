package mydebts.android.app.feature.participant

import mydebts.android.app.data.model.Participant

interface ParticipantScreen {

    fun showName(name: CharSequence)

    fun setNameEnabled(enabled: Boolean)

    fun showDebt(debt: Double)

    fun setPersonsSuggestions(persons: List<String>)

    fun setResult(result: Participant)

    fun finish()
}
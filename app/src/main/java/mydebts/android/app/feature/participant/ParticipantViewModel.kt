package mydebts.android.app.feature.participant

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.data.ParticipantsSource
import mydebts.android.app.data.PersonsSource
import mydebts.android.app.data.model.Participant
import mydebts.android.app.data.model.Person
import javax.inject.Inject

class ParticipantViewModel @Inject constructor(
        val participant: Participant,
        val personsSource: PersonsSource,
        val participantsSource: ParticipantsSource)
{

    private val participantSubject = PublishSubject.create<Participant>()

    fun setName(name: CharSequence) {
        setPerson(Person(name = name.toString()))
    }

    fun setPerson(person: Person) {
        participant.person = person
    }

    fun setDebt(debt: CharSequence) {
        participant.debt = debt.toString().toDoubleOrNull() ?: .0
    }

    fun observeParticipant(): Observable<Participant> {
        return participantSubject
    }

    fun onDoneClick() {
        participantSubject.onNext(participant)
    }
}
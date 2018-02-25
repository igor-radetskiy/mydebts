package mydebts.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mydebts.android.app.feature.event.EventActivity
import mydebts.android.app.feature.event.EventActivityModule
import mydebts.android.app.feature.participant.ParticipantDialogFragment
import mydebts.android.app.feature.participant.ParticipantModule
import mydebts.android.app.feature.person.PersonActivity
import mydebts.android.app.feature.person.PersonModule
import mydebts.android.app.feature.people.PeopleActivity
import mydebts.android.app.feature.people.PeopleModule
import mydebts.android.app.ui.date.DatePickerFragment
import mydebts.android.app.ui.events.EventsActivity

@Module
internal interface MyDebtsBindingModule {

    @ContributesAndroidInjector(modules = [mydebts.android.app.ui.events.EventsModule::class])
    fun bindEventsActivity(): EventsActivity

    @ContributesAndroidInjector(modules = [PeopleModule::class])
    fun bindPeopleActivity(): PeopleActivity

    @ContributesAndroidInjector(modules = [PersonModule::class])
    fun bindPersonActivity(): PersonActivity

    @ContributesAndroidInjector(modules = [ParticipantModule::class])
    fun bindParticipantDialogFragment(): ParticipantDialogFragment

    @ContributesAndroidInjector(modules = [EventActivityModule::class])
    fun bindEventActivity(): EventActivity

    @ContributesAndroidInjector fun bindDatePickerFragment(): DatePickerFragment
}

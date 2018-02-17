package mydebts.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mydebts.android.app.feature.event.EventActivity
import mydebts.android.app.feature.event.EventActivityModule
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.events.EventsModule
import mydebts.android.app.feature.main.MainActivity
import mydebts.android.app.feature.participant.ParticipantDialogFragment
import mydebts.android.app.feature.participant.ParticipantModule
import mydebts.android.app.feature.person.PersonActivity
import mydebts.android.app.feature.person.PersonModule
import mydebts.android.app.feature.people.PeopleActivity
import mydebts.android.app.feature.people.PeopleModule

@Module
internal interface MyDebtsBindingModule {

    @ContributesAndroidInjector fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector(modules = [EventsModule::class])
    fun bindEventsFragment(): EventsFragment

    @ContributesAndroidInjector(modules = [PeopleModule::class])
    fun bindPeopleActivity(): PeopleActivity

    @ContributesAndroidInjector(modules = [PersonModule::class])
    fun bindPersonActivity(): PersonActivity

    @ContributesAndroidInjector(modules = [ParticipantModule::class])
    fun bindParticipantDialogFragment(): ParticipantDialogFragment

    @ContributesAndroidInjector(modules = [EventActivityModule::class])
    fun bindEventActivity(): EventActivity
}

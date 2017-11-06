package mydebts.android.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mydebts.android.app.feature.event.EventFragment
import mydebts.android.app.feature.event.EventModule
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.events.EventsModule
import mydebts.android.app.feature.main.MainActivity
import mydebts.android.app.feature.participant.ParticipantDialogFragment
import mydebts.android.app.feature.participant.ParticipantModule
import mydebts.android.app.feature.person.PersonActivity
import mydebts.android.app.feature.person.PersonModule
import mydebts.android.app.feature.persons.PersonsActivity
import mydebts.android.app.feature.persons.PersonsModule

@Module
internal interface MyDebtsBindingModule {

    @ContributesAndroidInjector fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector(modules = arrayOf(EventsModule::class))
    fun bindEventsFragment(): EventsFragment

    @ContributesAndroidInjector(modules = arrayOf(EventModule::class))
    fun bindEventFragment(): EventFragment

    @ContributesAndroidInjector(modules = arrayOf(PersonsModule::class))
    fun bindPersonsActivity(): PersonsActivity

    @ContributesAndroidInjector(modules = arrayOf(PersonModule::class))
    fun bindPersonActivity(): PersonActivity

    @ContributesAndroidInjector(modules = arrayOf(ParticipantModule::class))
    fun bindParticipantDialogFragment(): ParticipantDialogFragment
}

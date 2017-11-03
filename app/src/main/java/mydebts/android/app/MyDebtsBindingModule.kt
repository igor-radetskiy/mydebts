package mydebts.android.app

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import mydebts.android.app.di.SubcomponentBuilder
import mydebts.android.app.di.SubcomponentBuilderKey
import mydebts.android.app.feature.event.EventFragment
import mydebts.android.app.feature.event.EventSubcomponent
import mydebts.android.app.feature.events.EventsFragment
import mydebts.android.app.feature.events.EventsSubcomponent
import mydebts.android.app.feature.participant.ParticipantDialogFragment
import mydebts.android.app.feature.participant.ParticipantSubcomponent
import mydebts.android.app.feature.person.PersonActivity
import mydebts.android.app.feature.person.PersonSubcomonent
import mydebts.android.app.feature.persons.PersonsFragment
import mydebts.android.app.feature.persons.PersonsSubcomponent

@Module(subcomponents = arrayOf(
        EventsSubcomponent::class,
        EventSubcomponent::class,
        PersonsSubcomponent::class,
        PersonSubcomonent::class,
        ParticipantSubcomponent::class))
internal interface MyDebtsBindingModule {
    @Binds
    @IntoMap
    @SubcomponentBuilderKey(EventsFragment::class)
    fun eventsSubcomponentBuilder(impl: EventsSubcomponent.Builder): SubcomponentBuilder

    @Binds
    @IntoMap
    @SubcomponentBuilderKey(EventFragment::class)
    fun eventSubcomponentBuilder(impl: EventSubcomponent.Builder): SubcomponentBuilder

    @Binds
    @IntoMap
    @SubcomponentBuilderKey(PersonsFragment::class)
    fun personsSubcomponentBuilder(impl: PersonsSubcomponent.Builder): SubcomponentBuilder

    @Binds
    @IntoMap
    @SubcomponentBuilderKey(PersonActivity::class)
    fun personSubcomponentBuilder(impl: PersonSubcomonent.Builder): SubcomponentBuilder

    @Binds
    @IntoMap
    @SubcomponentBuilderKey(ParticipantDialogFragment::class)
    fun participantSubcomponentBuilder(impl: ParticipantSubcomponent.Builder): SubcomponentBuilder
}

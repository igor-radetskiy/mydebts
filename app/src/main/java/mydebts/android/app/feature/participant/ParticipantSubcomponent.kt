package mydebts.android.app.feature.participant

import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(ParticipantScreen::class)
@Subcomponent(modules = arrayOf(ParticipantModule::class))
interface ParticipantSubcomponent {
    fun inject(dialogFragment: ParticipantDialogFragment)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance fun participant(participant: Participant?): Builder

        @BindsInstance fun dialogFragment(participantDialogFragment: ParticipantDialogFragment): Builder

        fun build(): ParticipantSubcomponent
    }
}
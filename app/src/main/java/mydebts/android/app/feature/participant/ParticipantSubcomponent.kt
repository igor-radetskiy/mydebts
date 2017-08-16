package mydebts.android.app.feature.participant

import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(ParticipantActivity::class)
@Subcomponent(modules = arrayOf(ParticipantModule::class))
interface ParticipantSubcomponent {
    fun inject(activity: ParticipantActivity)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance fun participant(participant: Participant?): Builder

        @BindsInstance fun activity(participantActivity: ParticipantActivity): Builder

        fun build(): ParticipantSubcomponent
    }
}
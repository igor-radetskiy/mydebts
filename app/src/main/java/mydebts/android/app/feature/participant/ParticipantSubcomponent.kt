package mydebts.android.app.feature.participant

import dagger.BindsInstance
import dagger.Subcomponent
import mydebts.android.app.data.model.Participant
import mydebts.android.app.di.SingleIn
import mydebts.android.app.di.SubcomponentBuilder

@SingleIn(ParticipantActivity::class)
@Subcomponent
interface ParticipantSubcomponent {
    fun inject(activity: ParticipantActivity)

    @Subcomponent.Builder
    interface Builder : SubcomponentBuilder {

        @BindsInstance
        fun participant(participant: Participant) : Builder

        fun build() : ParticipantSubcomponent
    }
}
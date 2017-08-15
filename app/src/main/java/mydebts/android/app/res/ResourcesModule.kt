package mydebts.android.app.res

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class ResourcesModule {
    @Provides @Singleton fun provideResources(impl: ResourcesImpl): Resources {
        return impl
    }
}
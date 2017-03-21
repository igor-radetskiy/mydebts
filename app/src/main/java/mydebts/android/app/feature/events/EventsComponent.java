package mydebts.android.app.feature.events;

import dagger.Component;
import mydebts.android.app.MyDebtsApplicationComponent;
import mydebts.android.app.di.ActivityScope;

@ActivityScope
@Component(modules = EventsModule.class, dependencies = MyDebtsApplicationComponent.class)
public interface EventsComponent {
    void inject(EventsActivity activity);
}

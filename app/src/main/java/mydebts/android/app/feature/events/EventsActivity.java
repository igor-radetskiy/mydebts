package mydebts.android.app.feature.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import mydebts.android.app.R;
import mydebts.android.app.db.Event;
import mydebts.android.app.di.SubcomponentBuilderResolver;
import mydebts.android.app.feature.addevent.EventActivity;

public class EventsActivity extends AppCompatActivity {

    @Inject EventsViewModel viewModel;
    @Inject RecyclerView eventsRecyclerView;
    @Inject View emptyView;

    private Disposable eventsDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ((EventsSubcomponent.Builder)SubcomponentBuilderResolver.resolve(this))
                .module(new EventsModule(this))
                .build().inject(this);

        eventsDisposable = viewModel.eventsSubject()
                .subscribe(this::accept);
    }

    @Override
    protected void onDestroy() {
        eventsDisposable.dispose();
        super.onDestroy();
    }

    void onAddEventClick() {
        startActivity(new Intent(this, EventActivity.class));
    }

    private void accept(List<Event> events) {
        boolean isEmpty = events == null || events.size() == 0;

        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        eventsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}

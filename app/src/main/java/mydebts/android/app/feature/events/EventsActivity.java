package mydebts.android.app.feature.events;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mydebts.android.app.MyDebtsApplication;
import mydebts.android.app.R;
import mydebts.android.app.db.Event;
import mydebts.android.app.di.SubcomponentBuilderResolver;

public class EventsActivity extends Activity implements Consumer<List<Event>> {

    @Inject EventsViewModel viewModel;
    @Inject RecyclerView eventsRecyclerView;
    @Inject View emptyView;

    private Disposable eventdsDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        SubcomponentBuilderResolver.resolve(this)
                .module(new EventsModule(this))
                .build().inject(this);

        eventdsDisposable = viewModel.eventsSubject()
                .subscribe(this);
    }

    @Override
    protected void onDestroy() {
        eventdsDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void accept(List<Event> events) throws Exception {
        boolean isEmpty = events == null || events.size() == 0;

        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        eventsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}

package mydebts.android.app.feature.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import mydebts.android.app.R;
import mydebts.android.app.data.EventsSource;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.di.SubcomponentBuilderResolver;
import mydebts.android.app.feature.main.MainRouter;
import mydebts.android.app.rx.RxUtil;

public class EventsFragment extends Fragment {

    @Inject EventsSource eventsSource;
    @Inject RxUtil rxUtil;

    RecyclerView eventsRecyclerView;
    View emptyView;

    EventsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((EventsSubcomponent.Builder) SubcomponentBuilderResolver.resolve(this))
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewBinder.bind(this);

        eventsSource.getAll()
                .compose(rxUtil.singleSchedulersTransformer())
                .subscribe(this::setEvents, this::setError);
    }

    void onAddEventClick() {
        ((MainRouter)getActivity()).navigateToNewEvent();
    }

    void onEventClick(Event event) {
        ((MainRouter)getActivity()).navigateToEvent(event);
    }

    private void setEvents(List<Event> events) {
        boolean isEmpty = events == null || events.size() == 0;

        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        eventsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        adapter.setEvents(events);
    }

    private void setError(Throwable throwable) {

    }
}

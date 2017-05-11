package mydebts.android.app.feature.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import mydebts.android.app.R;
import mydebts.android.app.data.db.EventsTable;
import mydebts.android.app.data.db.EventsTableDao;
import mydebts.android.app.data.db.ParticipantsTable;
import mydebts.android.app.data.db.ParticipantsTableDao;
import mydebts.android.app.data.db.PersonsTableDao;
import mydebts.android.app.data.model.Event;
import mydebts.android.app.di.SubcomponentBuilderResolver;
import mydebts.android.app.feature.main.MainRouter;

public class EventFragment extends Fragment {
    private static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private ParticipantsAdapter adapter;

    @Inject EventsTableDao eventDao;
    @Inject PersonsTableDao personDao;
    @Inject ParticipantsTableDao participantDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        ((EventSubcomponent.Builder)SubcomponentBuilderResolver.resolve(this))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_event, container, false);

        RecyclerView listParticipants = (RecyclerView)rootView.findViewById(R.id.list_participants);
        listParticipants.setLayoutManager(new LinearLayoutManager(listParticipants.getContext()));

        adapter = new ParticipantsAdapter();
        listParticipants.setAdapter(adapter);

        if (getArguments() != null && getArguments().containsKey(ARG_EVENT_ID)) {
            List<ParticipantsTable> participants = participantDao.queryRaw("WHERE " + ParticipantsTableDao.Properties.EventId.columnName + "=?", Long.toString(getArguments().getLong(ARG_EVENT_ID)));
            adapter.setItems(participants);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_date:
                return true;
            case R.id.action_save:
                saveEvent(adapter.getParticipants());
                return true;
            case R.id.action_delete:
                deleteEvent();
                return true;
            default:
                return false;
        }
    }

    private void saveEvent(List<ParticipantsTable> participants) {
        for (Iterator<ParticipantsTable> iterator = participants.iterator(); iterator.hasNext();) {
            ParticipantsTable participant = iterator.next();
            if (TextUtils.isEmpty(participant.peekPerson().getName())
                    || Math.abs(participant.getDebt()) < 0.001) {
                iterator.remove();
            }
        }

        if (participants.size() == 0) {
            return;
        }

        Date date = new Date();
        date.setTime(System.currentTimeMillis());

        EventsTable event = new EventsTable();
        event.setName(date.toString());
        event.setDate(date);

        long eventId = eventDao.insert(event);

        for (ParticipantsTable participant : participants) {
            participant.setEventId(eventId);
            participant.setPersonId(personDao.insert(participant.peekPerson()));
            participantDao.insert(participant);
        }

        ((MainRouter)getActivity()).navigateBack();
    }

    private void deleteEvent() {
        if (getArguments().containsKey(ARG_EVENT_ID)) {
            eventDao.deleteByKey(getArguments().getLong(ARG_EVENT_ID));
            List<ParticipantsTable> participants = participantDao.queryRaw("WHERE " + ParticipantsTableDao.Properties.EventId.columnName + "=?", Long.toString(getArguments().getLong(ARG_EVENT_ID)));
            for (ParticipantsTable participant : participants) {
                participant.delete();
            }
        }

        ((MainRouter)getActivity()).navigateBack();
    }

    public static EventFragment newInstance(@NonNull Event event) {
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, event.getId());

        EventFragment fragment = newInstance();
        fragment.setArguments(args);

        return fragment;
    }

    public static EventFragment newInstance() {
        return new EventFragment();
    }
}

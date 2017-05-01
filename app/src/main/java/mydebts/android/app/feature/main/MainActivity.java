package mydebts.android.app.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import mydebts.android.app.db.Event;
import mydebts.android.app.feature.event.EventFragment;
import mydebts.android.app.feature.events.EventsFragment;

public class MainActivity extends AppCompatActivity implements MainRouter {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigateToEvents();
    }

    @Override
    public void navigateToEvents() {
        replaceFragment(new EventsFragment());
    }

    @Override
    public void navigateToNewEvent() {
        replaceFragment(new EventFragment());
    }

    @Override
    public void navigateToEvent(Event event) {

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }
}

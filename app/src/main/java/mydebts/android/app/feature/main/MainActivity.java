package mydebts.android.app.feature.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
        replaceFragment(new EventsFragment(), false);
    }

    @Override
    public void navigateToNewEvent() {
        replaceFragment(EventFragment.newInstance(), true);
    }

    @Override
    public void navigateToEvent(@NonNull Event event) {
        replaceFragment(EventFragment.newInstance(event), true);
    }

    @Override
    public void navigateBack() {
        getSupportFragmentManager().popBackStack();
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}

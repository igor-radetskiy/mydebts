package mydebts.android.app.feature.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new EventFragment(), null)
                .commit();
    }
}

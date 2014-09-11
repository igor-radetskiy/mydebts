package iradetskyi.app.debt.data;

import android.content.Context;
import android.database.Cursor;

public class EventLoader extends DataLoader {

	private Event mEvent;
	
	public EventLoader(Context context) {
		super(context);
		
		mEvent = new Event(new DatabaseHandler(context));
	}

	@Override
	public Cursor loadInBackground() {
		Cursor retCursor = loadAllEvents();
		return retCursor;
	}
	
	private Cursor loadAllEvents() {
		return mEvent.readAll();
	}
}

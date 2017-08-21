package mydebts.android.app.extention

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toEventDateString(): String = SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault()).format(this)
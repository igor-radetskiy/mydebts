package mydebts.android.app.extention

import android.widget.TextView
import java.text.DecimalFormat

fun TextView.setCurrencyText(value: Double) {
    this.text = DecimalFormat("₴#.##").format(value)
}
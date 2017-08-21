package mydebts.android.app.extention

import android.widget.TextView
import java.text.DecimalFormat

fun TextView.setCurrencyText(value: Double) {
    this.text = value.toCurrencyString()
}

fun TextView.setDoubleText(value: Double) {
    this.text = value.toSimpleString()
}
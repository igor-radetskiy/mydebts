package mydebts.android.app.extention

import android.widget.TextView

fun TextView.setCurrencyText(value: Double) {
    this.text = value.toCurrencyString()
}

fun TextView.setDoubleText(value: Double) {
    this.text = value.toSimpleString()
}
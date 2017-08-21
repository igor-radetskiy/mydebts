package mydebts.android.app.extention

import java.text.DecimalFormat

fun Double.toCurrencyString(): String = DecimalFormat("₴#.##").format(this)

fun Double.toSimpleString(): String = DecimalFormat("#.##").format(this)


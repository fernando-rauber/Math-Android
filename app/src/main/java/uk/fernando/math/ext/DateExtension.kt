package uk.fernando.math.ext


import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToTime(): String {
    val parser = SimpleDateFormat("HH:mm", Locale.getDefault())
    return parser.format(this)
}

fun Date.formatToDate(): String {
    val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return parser.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = this
    cal2.time = date
    return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
}
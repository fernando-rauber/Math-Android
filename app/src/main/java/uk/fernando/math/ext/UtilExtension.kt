package uk.fernando.math.ext

import uk.fernando.math.R

fun Int.timerFormat(): String {
    val minutes = this / 60
    return "${minutes.toString().padStart(2, '0')}:${(this % 60).toString().padStart(2, '0')}"
}

fun Int.toFalseTrue(): Int {
    return if (this == 1) R.string.correct_boolean else R.string.incorrect_boolean
}
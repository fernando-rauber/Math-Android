package uk.fernando.math.ext

import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue

fun Int.mathOperator(): String {
    return when (getByValue(this)) {
        ADDITION -> "+"
        SUBTRACTION -> "-"
        MULTIPLICATION -> "x"
        DIVISION -> "/"
        PERCENTAGE -> "%"
        SQUARE -> "Square"
        else -> "FRACTION" // FRACTION
    }
}


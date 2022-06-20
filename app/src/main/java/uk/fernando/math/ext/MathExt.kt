package uk.fernando.math.ext

import androidx.compose.ui.graphics.Color
import uk.fernando.math.R
import uk.fernando.math.model.enum.Difficulty
import uk.fernando.math.model.enum.Difficulty.EASY
import uk.fernando.math.model.enum.Difficulty.MEDIUM
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue
import uk.fernando.math.ui.theme.star_green
import uk.fernando.math.ui.theme.star_orange
import uk.fernando.math.ui.theme.star_red

fun Int.mathOperator(): String {
    return when (getByValue(this)) {
        ADDITION -> "+"
        SUBTRACTION -> "-"
        MULTIPLICATION -> "x"
        DIVISION -> "-"
        PERCENTAGE -> "%"
        SQUARE -> "Square of"
        else -> "FRACTION" // FRACTION
    }
}

fun Int.mathOperatorIcon(): Int {
    return when (getByValue(this)) {
        ADDITION -> R.drawable.ic_math_addition
        SUBTRACTION -> R.drawable.ic_math_substraction
        MULTIPLICATION -> R.drawable.ic_math_multiplication
        DIVISION -> R.drawable.ic_math_division
        PERCENTAGE -> R.drawable.ic_math_percentage
        SQUARE -> R.drawable.ic_math_square_root
        GREATER_THAN -> R.drawable.ic_math_square_root
        else -> 1 // LESSER THAN
    }
}

fun Int.operatorIcon(): Int {
    return when (getByValue(this)) {
        ADDITION -> R.drawable.ic_plus
        SUBTRACTION -> R.drawable.ic_minus
        MULTIPLICATION -> R.drawable.ic_times
        DIVISION -> R.drawable.ic_divide
        PERCENTAGE -> R.drawable.ic_percent
        SQUARE -> R.drawable.ic_square_root
        GREATER_THAN -> R.drawable.ic_greater_than
        else -> R.drawable.ic_less_than // LESSER THAN
    }
}

fun Int.difficultyName(): Int {
    return when (Difficulty.getByValue(this)) {
        EASY -> R.string.easy
        MEDIUM -> R.string.medium
        else -> R.string.hard // Hard
    }
}

fun Int.difficultyColor(): Color {
    return when (Difficulty.getByValue(this)) {
        EASY -> star_green
        MEDIUM -> star_orange
        else -> star_red
    }
}
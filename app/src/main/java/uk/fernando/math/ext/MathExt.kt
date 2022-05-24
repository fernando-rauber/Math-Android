package uk.fernando.math.ext

import androidx.compose.ui.graphics.Color
import uk.fernando.math.R
import uk.fernando.math.model.enum.Difficulty
import uk.fernando.math.model.enum.Difficulty.*
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue

fun Int.mathOperator(): String {
    return when (getByValue(this)) {
        ADDITION -> "+"
        SUBTRACTION -> "-"
        MULTIPLICATION -> "x"
        DIVISION -> "/"
        PERCENTAGE -> "%"
        SQUARE -> "Square of"
        else -> "FRACTION" // FRACTION
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
        EASY -> Color.Green.copy(0.7f)
        MEDIUM -> Color.Yellow.copy(0.7f)
        else -> Color.Red.copy(0.7f) // Hard
    }
}
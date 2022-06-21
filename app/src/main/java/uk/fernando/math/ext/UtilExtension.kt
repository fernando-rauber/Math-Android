package uk.fernando.math.ext

import android.media.MediaPlayer
import androidx.navigation.NavController
import uk.fernando.math.R

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return tag.take(23)
    }

fun NavController.safeNav(direction: String) {
    try {
        this.navigate(direction)
    } catch (e: Exception) {
    }
}

fun Int.timerFormat(): String {
    val minutes = this / 60
    return "${minutes.toString().padStart(2, '0')}:${(this % 60).toString().padStart(2, '0')}"
}

fun Int.toFalseTrue(): Int {
    return if (this == 1) R.string.correct_boolean else R.string.incorrect_boolean
}

fun MediaPlayer.playAudio() {
    if (isPlaying) {
        stop()
        prepare()
    }
    start()
}

/**
 * Returns a list of random answer based on the original answer.
 */
fun Int.generateMultipleChoices(): List<Int> {
    val multipleChoice = mutableListOf<Int>()

    multipleChoice.add(this)

    for (i in 1..3) {
        multipleChoice.add(createFakeAnswer(this, multipleChoice))
    }

    return multipleChoice
}

private fun createFakeAnswer(answer: Int, existentAnswers: List<Int>): Int {
    val value = (1..9).random()

    val result = when ((1..2).random()) {
        1 -> answer.minus(value)
        else -> answer.plus(value)
    }

    return if (result < 0 || existentAnswers.contains(result)) // can't have the same answer
        createFakeAnswer(answer, existentAnswers)
    else
        result
}
package uk.fernando.math.viewmodel.solo

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import uk.fernando.logger.MyLogger
import uk.fernando.math.ext.TAG
import uk.fernando.math.repository.GameRepository
import uk.fernando.math.viewmodel.BaseGameViewModel


class GameViewModel(private val rep: GameRepository, private val logger: MyLogger) : BaseGameViewModel(rep, logger) {

    val chronometerSeconds = mutableStateOf(0)

    private val chronometer = object : CountDownTimer(7200000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if (!isGamePaused.value)
                chronometerSeconds.value++
        }

        override fun onFinish() {
            chronometerSeconds.value = 0
        }
    }

    fun createGame() {
        clean()

        launchDefault {
            val historyWithPlayer = rep.getOpenGame()

            history = historyWithPlayer!!.history

            // Get Players
            player1 = historyWithPlayer.playerList.first()

            maxQuestion = player1.questionList.count()

            nextQuestion()
        }
    }

    fun isMultipleChoice() = history.isMultipleChoice

    fun startChronometer() {
        chronometer.start()
    }

    fun registerAnswer(answer: Int): Boolean? {
        if (isGameFinished.value)
            return null

        val isAnswerCorrect = updateQuestion(answer, player1, currentQuestion.value!!)

        nextQuestion()

        return isAnswerCorrect
    }

    private fun nextQuestion() {
        if (player1.questionList.size > nextQuestionCounter.value) {
            currentQuestion.value = player1.questionList[nextQuestionCounter.value]
            nextQuestionCounter.value++
        } else {
            updateHistoryData()
        }
    }

    private fun updateHistoryData() {
        launchDefault {
            try {
                history.timer = chronometerSeconds.value
                chronometer.cancel()
                history.isFinished = true

                updateHistory(history)
                updatePlayer(player1)

                isGameFinished.value = true
            } catch (e: Exception) {
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error create history: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }
}
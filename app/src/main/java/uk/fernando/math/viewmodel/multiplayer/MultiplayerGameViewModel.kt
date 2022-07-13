package uk.fernando.math.viewmodel.multiplayer

import androidx.compose.runtime.mutableStateOf
import uk.fernando.logger.MyLogger
import uk.fernando.math.ext.TAG
import uk.fernando.math.repository.GameRepositoryImpl
import uk.fernando.math.viewmodel.BaseGameViewModel


class MultiplayerGameViewModel(private val rep: GameRepositoryImpl, private val logger: MyLogger) : BaseGameViewModel(rep, logger) {

    val player1Waiting = mutableStateOf(false)
    val player2Waiting = mutableStateOf(false)

    val player1Name = mutableStateOf("")
    val player2Name = mutableStateOf("")

    private var player1Counter = 1
    private var player2Counter = 1

    fun createGame() {
        clean()

        launchDefault {
            val historyWithPlayer = rep.getOpenGame()

            history = historyWithPlayer!!.history

            // Get Players
            player1 = historyWithPlayer.playerList[0]
            player2 = historyWithPlayer.playerList[1]

            player1Name.value = player1.name
            player2Name.value = player2.name

            maxQuestion = player1.questionList.count()

            nextQuestion()
        }
    }

    fun registerAnswerPlayer1(answer: Int?): Boolean? {
        if (isGameFinished.value)
            return null

        val isAnswerCorrect = updateQuestion(answer, player1, currentQuestion.value!!)

        player1Counter++

        if (player1Counter == player2Counter)
            nextQuestion()
        else
            player1Waiting.value = true

        return isAnswerCorrect
    }

    fun registerAnswerPlayer2(answer: Int?): Boolean? {
        if (isGameFinished.value)
            return null

        val isAnswerCorrect = updateQuestion(answer, player2, player2.questionList[nextQuestionCounter.value - 1])

        player2Counter++

        if (player1Counter == player2Counter)
            nextQuestion()
        else
            player2Waiting.value = true

        return isAnswerCorrect
    }

    private fun nextQuestion() {
        player1Waiting.value = false
        player2Waiting.value = false

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
                history.isFinished = true

                updateHistory(history)
                updatePlayer(player1)
                updatePlayer(player2)

                isGameFinished.value = true
            } catch (e: Exception) {
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error create history: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }
}




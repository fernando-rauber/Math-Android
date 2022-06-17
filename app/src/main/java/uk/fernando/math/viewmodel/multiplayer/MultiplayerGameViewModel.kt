package uk.fernando.math.viewmodel.multiplayer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.logger.MyLogger
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.PlayerQuestionEntity
import uk.fernando.math.ext.TAG
import uk.fernando.math.ext.mathOperator
import uk.fernando.math.model.Question
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.BaseViewModel


class MultiplayerGameViewModel(private val rep: HistoryRepository, private val logger: MyLogger) : BaseViewModel() {

    private var history = HistoryEntity()

    private var player1 = PlayerEntity()
    private var player2 = PlayerEntity()

    val player1Waiting = mutableStateOf(false)
    val player2Waiting = mutableStateOf(false)

    var maxQuestion = 0
    val nextQuestion = mutableStateOf(0)

    val currentQuestion: MutableState<Question?> = mutableStateOf(null)
    val historyId = mutableStateOf(0)
    val isGamePaused = mutableStateOf(false)

    fun createGame() {
        clean()

        history.difficulty = QuestionGenerator.getDifficulty()
        history.operatorList = QuestionGenerator.getMathOperatorList()
        history.multiplayer = true
        maxQuestion = QuestionGenerator.getQuestionList().count()

        player1.name = QuestionGenerator.getPlayer1()
        player2.name = QuestionGenerator.getPlayer2()

        nextQuestion()
    }

    fun pauseUnpauseGame() {
        isGamePaused.value = !isGamePaused.value
    }

    private fun clean() {
        try {
            history = HistoryEntity()
            historyId.value = 0
            nextQuestion.value = 0
            player1 = PlayerEntity()
            player2 = PlayerEntity()
        } catch (e: Exception) {
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to clean viewModel: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    fun registerAnswerPlayer1(answer: Int?): Boolean? {
        if (historyId.value != 0) // Game ended
            return null

        val nextQuestion = generateHistoryQuestion(answer, player1)

        if (nextQuestion)
            nextQuestion()
        else
            player1Waiting.value = true

        return answer == currentQuestion.value?.answer
    }

    fun registerAnswerPlayer2(answer: Int?): Boolean? {
        if (historyId.value != 0) // Game ended
            return null

        val nextQuestion = generateHistoryQuestion(answer, player2)

        if (nextQuestion)
            nextQuestion()
        else
            player2Waiting.value = true

        return answer == currentQuestion.value?.answer
    }

    private fun nextQuestion() {
        player1Waiting.value = false
        player2Waiting.value = false

        if (QuestionGenerator.getQuestionList().size > nextQuestion.value) {
            currentQuestion.value = QuestionGenerator.getQuestionList()[nextQuestion.value]
            nextQuestion.value++
        } else {
            insertHistory()
        }
    }

    private fun insertHistory() {
        launchDefault {
            try {
                historyId.value = rep.insertHistory(HistoryWithPLayers(history, listOf(player1, player2)))
                QuestionGenerator.clean()
            } catch (e: Exception) {
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error create history: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun generateHistoryQuestion(answer: Int?, player: PlayerEntity): Boolean {
        currentQuestion.value?.let { q ->

            // score counter
            if (answer == q.answer)
                player.correct++
            else
                player.incorrect++

            val question = PlayerQuestionEntity(
                question = "${q.first} ${q.operator.mathOperator()} ${q.second}",
                answer = answer,
                correctAnswer = q.answer
            )

            player.questionList.add(question)
        }

        return player1.questionList.size == player2.questionList.size
    }

}




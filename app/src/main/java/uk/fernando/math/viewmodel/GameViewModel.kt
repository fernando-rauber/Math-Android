package uk.fernando.math.viewmodel

import android.os.CountDownTimer
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


class GameViewModel(private val rep: HistoryRepository, private val logger: MyLogger) : BaseViewModel() {

    private var history = HistoryEntity()
    private var player1 = PlayerEntity()

    var maxQuestion = 0
    val nextQuestion = mutableStateOf(0)

    val currentQuestion: MutableState<Question?> = mutableStateOf(null)
    val historyId = mutableStateOf(0)
    val chronometerSeconds = mutableStateOf(0)
    val isGamePaused = mutableStateOf(false)

    private val chronometer = object : CountDownTimer(30000000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            if (!isGamePaused.value)
                chronometerSeconds.value++
        }

        override fun onFinish() {
        }
    }

    fun createGame() {
        clean()

        history.difficulty = QuestionGenerator.getDifficulty()
        history.operatorList = QuestionGenerator.getMathOperatorList()
        maxQuestion = QuestionGenerator.getQuestionList().count()

        nextQuestion()
    }

    fun startChronometer() {
        chronometer.start()
    }

    fun pauseUnpauseGame() {
        isGamePaused.value = !isGamePaused.value
    }

    private fun clean() {
        try {
            history = HistoryEntity()
            nextQuestion.value = 0
            chronometerSeconds.value = 0
            player1 = PlayerEntity()
        } catch (e: Exception) {
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to clean viewModel: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    fun registerAnswer(answer: Int): Boolean? {
        if (historyId.value != 0) // Test ended
            return null

        createHistoryQuestion(answer)

        nextQuestion()

        return answer == currentQuestion.value?.answer
    }

    private fun nextQuestion() {
        if (QuestionGenerator.getQuestionList().size > nextQuestion.value) {
            currentQuestion.value = QuestionGenerator.getQuestionList()[nextQuestion.value]
            nextQuestion.value++
        } else {
            createHistory()
        }
    }

    private fun createHistory() {
        launchIO {
            try {
                chronometer.cancel()
                history.timer = chronometerSeconds.value
                historyId.value = rep.insertHistory(HistoryWithPLayers(history, listOf(player1)))
                QuestionGenerator.clean()
            } catch (e: Exception) {
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error create history: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun createHistoryQuestion(answer: Int) {
        launchIO {
            currentQuestion.value?.let { q ->

                // score counter
                if (answer == q.answer)
                    player1.correct++
                else
                    player1.incorrect++

                val question = PlayerQuestionEntity(
                    question = "${q.first} ${q.operator.mathOperator()} ${q.second}",
                    answer = answer,
                    correctAnswer = q.answer
                )

                player1.questionList.add(question)
            }
        }
    }

}




package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.logger.MyLogger
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.repository.GameRepository
import uk.fernando.util.ext.TAG


open class BaseGameViewModel(private val rep: GameRepository, private val logger: MyLogger) : BaseViewModel() {

    lateinit var history: HistoryEntity
    lateinit var player1: PlayerEntity
    lateinit var player2: PlayerEntity

    var maxQuestion = 0
    val nextQuestionCounter = mutableStateOf(0)

    val currentQuestion: MutableState<QuestionEntity?> = mutableStateOf(null)
    val isGameFinished = mutableStateOf(false)
    val isGamePaused = mutableStateOf(false)

    fun getHistoryId() = history.id

    fun pauseUnpauseGame() {
        isGamePaused.value = !isGamePaused.value
    }

    open fun clean() {
        try {
            nextQuestionCounter.value = 0
        } catch (e: Exception) {
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to clean viewModel: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    fun updateQuestion(answer: Int?, player: PlayerEntity, question: QuestionEntity): Boolean {
        // score counter
        if (answer == question.correctAnswer)
            player.correct++
        else
            player.incorrect++

        question.answer = answer

        updateQuestion(question)

        return answer == question.correctAnswer
    }

    private fun updateQuestion(question: QuestionEntity) {
        launchDefault {
            rep.updateQuestion(question)
        }
    }

    fun updatePlayer(player: PlayerEntity) {
        launchDefault {
            rep.updatePlayer(player)
        }
    }

    fun updateHistory(history: HistoryEntity) {
        launchDefault {
            rep.updateHistory(history)
        }
    }
}
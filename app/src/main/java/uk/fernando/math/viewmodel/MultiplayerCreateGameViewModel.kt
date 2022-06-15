package uk.fernando.math.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.math.ext.TAG
import uk.fernando.math.util.QuestionGenerator


class MultiplayerCreateGameViewModel(private val logger: MyLogger) : BaseViewModel() {

    private val operatorOptions = mutableListOf(1, 2, 3, 4)
    private var quantity = 10
    private var player1 = "Player 1"
    private var player2 = "Player 2"
    private var difficulty = 1 // Easy
    val loading = mutableStateOf(false)

    fun setPlayer1(name: String) {
        this.player1 = name
    }

    fun setPlayer2(name: String) {
        this.player2 = name
    }

    fun setMathOptions(option: Int) {
        if (operatorOptions.contains(option))
            operatorOptions.remove(option)
        else
            operatorOptions.add(option)
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }

    fun setDifficulty(difficulty: Int) {
        this.difficulty = difficulty
    }

    fun generateQuestion() = flow {
        loading.value = true
        kotlin.runCatching {
            QuestionGenerator.setPlayers(player1, player2)
            val finished = QuestionGenerator.generateQuestions(operatorOptions, quantity, true, difficulty)

            loading.value = !finished
            emit(finished)
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to generate questions: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
            emit(false)
        }
    }
}
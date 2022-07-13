package uk.fernando.math.viewmodel.multiplayer

import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.ext.TAG
import uk.fernando.math.repository.HistoryRepositoryImpl
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.BaseCreateGameViewModel
import java.util.*


class MultiplayerCreateGameViewModel(private val rep: HistoryRepositoryImpl, private val logger: MyLogger) : BaseCreateGameViewModel() {

    private var player1 = "Player 1"
    private var player2 = "Player 2"

    fun setPlayer1(name: String) {
        this.player1 = name
    }

    fun setPlayer2(name: String) {
        this.player2 = name
    }

    fun generateQuestion() = flow {
        loading.value = true
        kotlin.runCatching {
            // Delete Previous Game if still open
            rep.deleteOpenGame()

            val player1 = PlayerEntity(name = player1)
            val player2 = PlayerEntity(name = player2)
            val history = HistoryEntity(
                date = Date(),
                difficulty = difficultyLevel,
                operatorList = operatorOptions,
                multiplayer = true
            )

            val questionList = QuestionGenerator().generateQuestions(operatorOptions, quantityQuestion, difficultyLevel)
            player1.questionList.addAll(questionList)
            player2.questionList.addAll(questionList)

            rep.insertHistory(HistoryWithPLayers(history, listOf(player1, player2)))

            loading.value = false
            resetViewModel()
            emit(true)
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to generate questions: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
            emit(false)
        }
    }
}
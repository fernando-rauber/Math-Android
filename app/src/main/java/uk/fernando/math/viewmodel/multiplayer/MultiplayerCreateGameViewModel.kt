package uk.fernando.math.viewmodel.multiplayer

import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.ext.TAG
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.usecase.GamePrefsUseCase
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.BaseCreateGameViewModel
import java.util.*


class MultiplayerCreateGameViewModel(
    private val rep: HistoryRepository,
    private val logger: MyLogger,
    private val useCase: GamePrefsUseCase
) : BaseCreateGameViewModel(useCase) {

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

            val difficulty = useCase.getDifficulty()
            val operatorOptions = useCase.getOperator()

            val player1 = PlayerEntity(name = player1)
            val player2 = PlayerEntity(name = player2)
            val history = HistoryEntity(
                date = Date(),
                difficulty = difficulty,
                operatorList = operatorOptions,
                multiplayer = true
            )

            val questionList = QuestionGenerator().generateQuestions(operatorOptions, useCase.getQuantity(), difficulty)
            player1.questionList.addAll(questionList)
            player2.questionList.addAll(questionList)

            rep.insertHistory(HistoryWithPLayers(history, listOf(player1, player2)))

            loading.value = false
            emit(true)
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to generate questions: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
            emit(false)
        }
    }
}
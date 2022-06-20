package uk.fernando.math.viewmodel.solo

import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.ext.TAG
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.BaseCreateGameViewModel
import java.util.*


class CreateGameViewModel(private val rep: HistoryRepository, private val logger: MyLogger) : BaseCreateGameViewModel() {

    fun generateQuestion() = flow {
        loading.value = true
        kotlin.runCatching {
            // Delete Previous Game if still open
            rep.deleteOpenGame()

            val player = PlayerEntity()
            val history = HistoryEntity(
                date = Date(),
                difficulty = difficultyLevel,
                operatorList = operatorOptions,
                isMultipleChoice = isMultipleChoice
            )

            player.questionList.addAll(QuestionGenerator().generateQuestions(operatorOptions, quantityQuestion, difficultyLevel))

            rep.insertHistory(HistoryWithPLayers(history, listOf(player)))

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




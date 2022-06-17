package uk.fernando.math.viewmodel.solo

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.math.ext.TAG
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.BaseViewModel


class CreateGameViewModel(private val logger: MyLogger) : BaseViewModel() {

    private val operatorOptions = mutableListOf(1, 2, 3, 4)
    private var quantity = 10
    private var isMultipleChoice = true
    private var difficulty = 1 // Easy
    val loading = mutableStateOf(false)

    fun setMathOptions(option: Int) {
        if (operatorOptions.contains(option))
            operatorOptions.remove(option)
        else
            operatorOptions.add(option)
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }

    fun setTypeAnswer(isMultipleChoice: Boolean) {
        this.isMultipleChoice = isMultipleChoice
    }

    fun setDifficulty(difficulty: Int) {
        this.difficulty = difficulty
    }

    fun generateQuestion() = flow {
        loading.value = true
        kotlin.runCatching {
            val finished = QuestionGenerator.generateQuestions(operatorOptions, quantity, isMultipleChoice, difficulty)

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




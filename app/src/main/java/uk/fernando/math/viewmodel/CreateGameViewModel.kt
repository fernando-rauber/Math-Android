package uk.fernando.math.viewmodel

import kotlinx.coroutines.flow.flow
import uk.fernando.math.util.QuestionGenerator


class CreateGameViewModel() : BaseViewModel() {

    private val operatorOptions = mutableListOf(1, 2, 3, 4)
    private var quantity = 10
    private var typeAnswer = 2 // Multiple choice
    private var difficulty = 2 // Medium


    fun setMathOptions(option: Int) {
        if (operatorOptions.contains(option))
            operatorOptions.remove(option)
        else
            operatorOptions.add(option)
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }

    fun setTypeAnswer(typeAnswer: Int) {
        this.typeAnswer = typeAnswer
    }

    fun setDifficulty(difficulty: Int) {
        this.difficulty = difficulty
    }

    fun generateQuestion() = flow {
        // Loading
        QuestionGenerator.generateQuestions(operatorOptions, quantity, typeAnswer, difficulty)

        emit(true)
    }
}




package uk.fernando.math.viewmodel

import androidx.compose.runtime.mutableStateOf


open class BaseCreateGameViewModel : BaseViewModel() {

    val operatorOptions = mutableListOf(1, 2, 3, 4)
    var quantityQuestion = 10
    var isMultipleChoice = true
    var difficultyLevel = 1 // Easy
    val loading = mutableStateOf(false)
    val isGameValid = mutableStateOf(true)

    fun setMathOptions(option: Int) {
        if (operatorOptions.contains(option))
            operatorOptions.remove(option)
        else
            operatorOptions.add(option)

        isGameValid.value = operatorOptions.size > 0
    }

    fun setQuantity(quantity: Int) {
        this.quantityQuestion = quantity
    }

    fun setTypeAnswer(isMultipleChoice: Boolean) {
        this.isMultipleChoice = isMultipleChoice
    }

    fun setDifficulty(difficulty: Int) {
        this.difficultyLevel = difficulty
    }

    fun resetViewModel() {
        operatorOptions.clear()
        operatorOptions.addAll(listOf(1, 2, 3, 4))
        quantityQuestion = 10
        isMultipleChoice = true
        difficultyLevel = 1
    }
}




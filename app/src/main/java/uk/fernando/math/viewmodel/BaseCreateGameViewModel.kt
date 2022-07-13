package uk.fernando.math.viewmodel

import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.usecase.GamePrefsUseCase


open class BaseCreateGameViewModel(private val useCase: GamePrefsUseCase) : BaseViewModel() {

    val loading = mutableStateOf(false)
    val isGameValid = mutableStateOf(false)

    init {
        launchDefault { isGameValid.value = useCase.getOperator().isNotEmpty() }
    }

    fun setMathOptions(option: Int) {
        launchDefault { isGameValid.value = useCase.storeOperator(option) }
    }

    fun setQuantity(quantity: Int) {
        launchDefault { useCase.storeQuantity(quantity) }
    }

    fun setTypeAnswer(isMultipleChoice: Boolean) {
        launchDefault { useCase.storeMultipleChoice(isMultipleChoice) }
    }

    fun setDifficulty(difficulty: Int) {
        launchDefault { useCase.storeDifficulty(difficulty) }
    }
}




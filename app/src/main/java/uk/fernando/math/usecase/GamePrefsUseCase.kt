package uk.fernando.math.usecase

import kotlinx.coroutines.flow.first
import uk.fernando.math.datastore.GamePrefsStore


class GamePrefsUseCase(private val prefs: GamePrefsStore) {

    private var operatorList = mutableListOf<Int>()

    suspend fun storeOperator(operator: Int) : Boolean{
        if(operatorList.isEmpty())
            operatorList = getOperator().toMutableList()

        if (operatorList.contains(operator))
            operatorList.remove(operator)
        else
            operatorList.add(operator)

        prefs.storeOperator(operatorList)

        return operatorList.isNotEmpty()
    }

    suspend fun storeOperatorList(operators: List<Int>) {
        prefs.storeOperator(operators)
    }

    suspend fun storeQuantity(value: Int) {
        prefs.storeQuantity(value)
    }

    suspend fun storeMultipleChoice(value: Boolean) {
        prefs.storeMultipleChoice(value)
    }

    suspend fun storeDifficulty(value: Int) {
        prefs.storeDifficulty(value)
    }

    suspend fun getDifficulty() = prefs.difficulty().first()
    suspend fun isMultipleChoice() = prefs.isMultipleChoice().first()
    suspend fun getQuantity() = prefs.quantity().first()
    suspend fun getOperator() = prefs.getOperators().first()
}
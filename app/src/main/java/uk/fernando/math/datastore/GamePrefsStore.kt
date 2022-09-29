package uk.fernando.math.datastore

import kotlinx.coroutines.flow.Flow

interface GamePrefsStore {

    fun getOperators(): Flow<List<Int>>
    fun quantity(): Flow<Int>
    fun isMultipleChoice(): Flow<Boolean>
    fun difficulty(): Flow<Int>

    suspend fun storeOperator(value: List<Int>)
    suspend fun storeQuantity(value: Int)
    suspend fun storeMultipleChoice(value: Boolean)
    suspend fun storeDifficulty(value: Int)
}
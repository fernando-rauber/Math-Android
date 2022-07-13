package uk.fernando.math.repository

import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

interface GameRepository {

    suspend fun getOpenGame() : HistoryWithPLayers?
    suspend fun updateHistory(history: HistoryEntity)
    suspend fun updatePlayer(player: PlayerEntity)
    suspend fun updateQuestion(question: QuestionEntity)
}
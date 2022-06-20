package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.GameDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

class GameRepository(private val dao: GameDao) {

    suspend fun getOpenGame() = withContext(Dispatchers.IO) {
        dao.getOpenGame()
    }

    suspend fun updateHistory(history: HistoryEntity) = withContext(Dispatchers.IO) {
        dao.updateHistory(history)
    }

    suspend fun updatePlayer(player: PlayerEntity) = withContext(Dispatchers.IO) {
        dao.updatePlayer(player)
    }

    suspend fun updateQuestion(question: QuestionEntity) = withContext(Dispatchers.IO) {
        dao.updateQuestion(question)
    }

}

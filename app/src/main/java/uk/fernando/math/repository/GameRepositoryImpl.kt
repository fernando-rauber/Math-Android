package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.GameDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

open class GameRepositoryImpl(private val dao: GameDao) : GameRepository {

    override suspend fun getOpenGame() = withContext(Dispatchers.IO) {
        dao.getOpenGame()
    }

    override suspend fun updateHistory(history: HistoryEntity) = withContext(Dispatchers.IO) {
        dao.updateHistory(history)
    }

    override suspend fun updatePlayer(player: PlayerEntity) = withContext(Dispatchers.IO) {
        dao.updatePlayer(player)
    }

    override suspend fun updateQuestion(question: QuestionEntity) = withContext(Dispatchers.IO) {
        dao.updateQuestion(question)
    }
}

package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.GameDao
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryWithPLayers

class HistoryRepositoryImpl(private val dao: HistoryDao, private val gameDao: GameDao) : HistoryRepository {

    override suspend fun insertHistory(history: HistoryWithPLayers) = withContext(Dispatchers.IO) {
        dao.insertHistory(history).toInt()
    }

    override fun getAllHistory(isMultiplayer: Boolean) = dao.getHistory(isMultiplayer)

    override suspend fun getQuestionByHistory(id: Int) = withContext(Dispatchers.IO) {
        dao.getHistoryWithFriendsById(id)
    }

    override suspend fun deleteOpenGame() {
        withContext(Dispatchers.IO) {
            val game = gameDao.getOpenGame()
            if (game != null)
                gameDao.deleteGame(game)
        }
    }
}

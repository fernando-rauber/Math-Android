package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.MultiplayerDao
import uk.fernando.math.database.entity.multiplayer.HistoryWithPLayers

class MultiplayerRepository(private val dao: MultiplayerDao) {

    suspend fun insertHistory(history: HistoryWithPLayers) = withContext(Dispatchers.IO) {
        dao.insertHistory(history).toInt()
    }

    fun getAllHistory() = dao.getHistory()

    suspend fun getQuestionByHistory(id: Int) = withContext(Dispatchers.IO) {
        dao.getHistoryWithFriendsById(id)
    }
}
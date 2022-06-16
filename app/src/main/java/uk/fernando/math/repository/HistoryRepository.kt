package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryWithPLayers

class HistoryRepository(private val dao: HistoryDao) {

    suspend fun insertHistory(history: HistoryWithPLayers) = withContext(Dispatchers.IO) {
        dao.insertHistory(history).toInt()
    }

    fun getAllHistory(isMultiplayer: Boolean) = dao.getHistory(isMultiplayer)

    suspend fun getQuestionByHistory(id: Int) = withContext(Dispatchers.IO) {
        dao.getHistoryWithFriendsById(id)
    }

}

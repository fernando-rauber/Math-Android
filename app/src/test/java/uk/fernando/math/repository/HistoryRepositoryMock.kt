package uk.fernando.math.repository

import androidx.paging.PagingSource
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity

open class HistoryRepositoryMock : HistoryRepository {

    private fun historyList(): List<HistoryWithPLayers> {
        val player = PlayerEntity()
        val history = HistoryEntity()
        val playerQuestion = HistoryWithPLayers(history, listOf(player, player))

        return listOf(playerQuestion)
    }

    override suspend fun insertHistory(history: HistoryWithPLayers): Int {
        return 5
    }

    override fun getAllHistory(isMultiplayer: Boolean): PagingSource<Int, HistoryWithPLayers> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionByHistory(id: Int): HistoryWithPLayers {
        return historyList().first()
    }

    override suspend fun deleteOpenGame() {

    }
}


package uk.fernando.math.repository

import androidx.paging.PagingSource
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.PlayerQuestionEntity

open class HistoryRepositoryMock : HistoryDao {

    override fun insert(item: HistoryEntity): Long {
        return 5
    }

    override fun getHistory(isMultiplayer: Boolean): PagingSource<Int, HistoryWithPLayers> {
        TODO("Not yet implemented")
    }

    override fun insertPlayer(item: PlayerEntity): Long {
        return 3
    }

    override fun insertQuestion(item: PlayerQuestionEntity) {

    }

    override fun getHistoryById(historyId: Int): HistoryWithPLayers {
        return historyList().first()
    }

    override fun getQuestionByFriend(playerID: Long): List<PlayerQuestionEntity> {
        TODO("Not yet implemented")
    }


    private fun historyList(): List<HistoryWithPLayers> {
        val player = PlayerEntity()
        val history = HistoryEntity()
        val playerQuestion = HistoryWithPLayers(history, listOf(player, player))

        return listOf(playerQuestion)
    }
}


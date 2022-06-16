package uk.fernando.math.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import uk.fernando.math.database.entity.multiplayer.HistoryWithPLayers
import uk.fernando.math.database.entity.multiplayer.MultiplayerHistoryEntity
import uk.fernando.math.database.entity.multiplayer.PlayerEntity
import uk.fernando.math.database.entity.multiplayer.PlayerQuestionEntity

@Dao
interface MultiplayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MultiplayerHistoryEntity): Long

    @Query("SELECT * FROM ${MultiplayerHistoryEntity.NAME} ORDER BY date DESC")
    fun getHistory(): PagingSource<Int, HistoryWithPLayers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(item: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(item: PlayerQuestionEntity)

    @Transaction
    fun insertHistory(history: HistoryWithPLayers): Long {
        val historyID = insert(history.history)

        history.playerList.forEach { player ->
            player.historyId = historyID
            insertPlayer(player).let { playeID ->

                player.questionList.forEach { question ->
                    question.playerId = playeID
                    insertQuestion(question)
                }
            }
        }

        return historyID
    }

    @Query("SELECT * FROM ${MultiplayerHistoryEntity.NAME} WHERE id = :historyId")
    fun getHistoryById(historyId: Int): HistoryWithPLayers

    @Query("SELECT * FROM ${PlayerQuestionEntity.NAME} WHERE player_id = :playerID")
    fun getQuestionByFriend(playerID: Long): List<PlayerQuestionEntity>

    @Transaction
    fun getHistoryWithFriendsById(historyId: Int): HistoryWithPLayers {
        return getHistoryById(historyId).apply {
            this.playerList.forEach {
                it.questionList.addAll(getQuestionByFriend(it.id!!))
            }
        }
    }
}
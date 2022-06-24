package uk.fernando.math.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryEntity): Long

    @Transaction
    @Query("SELECT * FROM ${HistoryEntity.NAME} WHERE multiplayer = :isMultiplayer AND is_finished = 1 ORDER BY date DESC")
    fun getHistory(isMultiplayer: Boolean): PagingSource<Int, HistoryWithPLayers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(item: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(item: QuestionEntity)

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

    @Transaction
    @Query("SELECT * FROM ${HistoryEntity.NAME} WHERE id = :historyId")
    fun getHistoryById(historyId: Int): HistoryWithPLayers

    @Query("SELECT * FROM ${QuestionEntity.NAME} WHERE player_id = :playerID")
    fun getQuestionByFriend(playerID: Long): List<QuestionEntity>

    @Transaction
    fun getHistoryWithFriendsById(historyId: Int): HistoryWithPLayers {
        return getHistoryById(historyId).apply {
            this.playerList.forEach {
                it.questionList.addAll(getQuestionByFriend(it.id!!))
            }
        }
    }
}
package uk.fernando.math.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithQuestions
import uk.fernando.math.database.entity.QuestionEntity
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
}
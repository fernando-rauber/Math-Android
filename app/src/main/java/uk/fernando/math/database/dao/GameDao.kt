package uk.fernando.math.database.dao

import androidx.room.*
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

@Dao
interface GameDao {

    @Query("SELECT * FROM ${QuestionEntity.NAME} WHERE player_id = :playerID")
    fun getQuestionByPlayer(playerID: Long): List<QuestionEntity>

    @Transaction
    @Query("SELECT * FROM ${HistoryEntity.NAME} WHERE is_finished = 0")
    fun getHistoryOpen(): HistoryWithPLayers?

    @Transaction
    fun getOpenGame(): HistoryWithPLayers? {
        return getHistoryOpen().apply {
            this?.playerList?.forEach {
                it.questionList.addAll(getQuestionByPlayer(it.id!!))
            }
        }
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHistory(item: HistoryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlayer(item: PlayerEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateQuestion(item: QuestionEntity)

    @Transaction
    fun deleteGame(historyWithPLayers: HistoryWithPLayers) {
        deleteHistory(historyWithPLayers.history)
    }

    @Delete
    fun deleteHistory(item: HistoryEntity)

    @Delete
    fun deletePlayer(item: PlayerEntity)

    @Delete
    fun deleteQuestion(item: List<QuestionEntity>)

}
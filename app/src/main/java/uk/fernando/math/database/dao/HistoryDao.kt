package uk.fernando.math.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithQuestions
import uk.fernando.math.database.entity.QuestionEntity

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryEntity): Long

    @Query("SELECT * FROM ${HistoryEntity.NAME} ORDER BY date DESC")
    fun getHistory(): PagingSource<Int, HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(item: QuestionEntity)

    @Query("SELECT * FROM ${HistoryEntity.NAME} WHERE id = :historyId")
    fun getQuestionsByHistory(historyId: Int): HistoryWithQuestions

}
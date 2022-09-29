package uk.fernando.math.repository

import androidx.paging.PagingSource
import uk.fernando.math.database.entity.HistoryWithPLayers

interface HistoryRepository {

    suspend fun insertHistory(history: HistoryWithPLayers) : Int
    fun getAllHistory(isMultiplayer: Boolean) : PagingSource<Int, HistoryWithPLayers>
    suspend fun getQuestionByHistory(id: Int) : HistoryWithPLayers
    suspend fun deleteOpenGame()
}
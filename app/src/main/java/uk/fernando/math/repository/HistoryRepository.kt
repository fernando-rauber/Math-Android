package uk.fernando.math.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryWithQuestions

class HistoryRepository(private val dao: HistoryDao) {

    suspend fun insertHistory(history: HistoryWithQuestions) = withContext(Dispatchers.IO) {
        val historyID = dao.insert(history.history)

        history.questionList.forEach { question ->
            question.historyId = historyID
            dao.insertQuestion(question)
        }
    }

    suspend fun getAllHistory() = withContext(Dispatchers.IO) {
        dao.getHistory()
    }

    suspend fun getQuestionByHistory(id: Long) = withContext(Dispatchers.IO) {
        dao.getQuestionsByHistory(id)
    }

}

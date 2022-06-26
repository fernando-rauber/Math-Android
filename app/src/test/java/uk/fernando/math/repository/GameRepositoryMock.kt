package uk.fernando.math.repository

import uk.fernando.math.database.dao.GameDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

open class GameRepositoryMock : GameDao {

    private fun historyList(): List<HistoryWithPLayers> {
        val question1 = QuestionEntity(1, "", "", 1, 1, 1, 1)
        val question2 = QuestionEntity(2, "", "", 1, 1, 1, 1)
        val question3 = QuestionEntity(2, "", "", 1, 1, 1, 1)

        val player = PlayerEntity()
        player.questionList.addAll(listOf(question1, question2, question3))

        val history = HistoryEntity(id = 5)
        val playerQuestion = HistoryWithPLayers(history, listOf(player, player))

        return listOf(playerQuestion)
    }

    override fun getQuestionByPlayer(playerID: Long): List<QuestionEntity> {
        return listOf(QuestionEntity(5, "", "", 1, 1, 1, 1))
    }

    override fun getHistoryOpen(): HistoryWithPLayers? {
        return historyList().first()
    }

    override fun updateHistory(item: HistoryEntity) {

    }

    override fun updatePlayer(item: PlayerEntity) {

    }

    override fun updateQuestion(item: QuestionEntity) {

    }

    override fun deleteHistory(item: HistoryEntity) {

    }

    override fun deletePlayer(item: PlayerEntity) {

    }

    override fun deleteQuestion(item: List<QuestionEntity>) {

    }
}


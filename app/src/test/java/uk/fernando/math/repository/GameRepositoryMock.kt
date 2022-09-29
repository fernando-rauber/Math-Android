package uk.fernando.math.repository

import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithPLayers
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity

open class GameRepositoryMock : GameRepository {

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

    override suspend fun getOpenGame(): HistoryWithPLayers? {
        return historyList().first()
    }

    override suspend fun updateHistory(history: HistoryEntity) {
    }

    override suspend fun updatePlayer(player: PlayerEntity) {
    }

    override suspend fun updateQuestion(question: QuestionEntity) {
    }
}


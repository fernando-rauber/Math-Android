package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.flow
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.HistoryWithQuestions
import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.ext.mathOperator
import uk.fernando.math.model.Question
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.util.QuestionGenerator


class GameViewModel(val rep: HistoryRepository) : BaseViewModel() {

    private var history = HistoryEntity()
    private val historyQuestion = mutableListOf<QuestionEntity>()

    private var nextQuestion = 0

    val currentQuestion: MutableState<Question?> = mutableStateOf(null)

    init {
        QuestionGenerator.generateQuestions(listOf(1, 2), 3, 2, 1)

        history.difficulty = QuestionGenerator.getDifficulty()

        nextQuestion()
    }

    fun checkAnswer(answer: Int) = flow {
        if (answer == currentQuestion.value?.answer) {
            history.correct++

            createHistoryQuestion(answer)

            emit(nextQuestion())
        } else {
            if (historyQuestion.size < nextQuestion) {
                createHistoryQuestion(answer)
                history.incorrect++
            }
        }
    }

    private fun nextQuestion(): Boolean {
        return if (QuestionGenerator.getQuestionList().size > nextQuestion) {
            currentQuestion.value = QuestionGenerator.getQuestionList()[nextQuestion]
            nextQuestion++
            false
        } else {
            launchDefault {
                rep.insertHistory(HistoryWithQuestions(history, historyQuestion))
                QuestionGenerator.clean()
            }
            true
        }
    }

    private fun createHistoryQuestion(answer: Int) {
        currentQuestion.value?.let { q ->

            //this avoid to duplicate the question
            if (historyQuestion.size >= nextQuestion)
                return

            val question = QuestionEntity(
                question = "${q.first} ${q.operator.mathOperator()} ${q.second}",
                answer = answer,
                correctAnswer = q.answer
            )

            historyQuestion.add(question)
        }
    }

}




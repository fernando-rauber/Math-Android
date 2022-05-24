package uk.fernando.math.viewmodel

import android.os.CountDownTimer
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


class GameViewModel(private val rep: HistoryRepository) : BaseViewModel() {

    private var history = HistoryEntity()
    private val historyQuestion = mutableListOf<QuestionEntity>()

    private var nextQuestion = 0

    val currentQuestion: MutableState<Question?> = mutableStateOf(null)
    val historyId = mutableStateOf(0)
    val chronometerSeconds = mutableStateOf(0)

    private val chronometer = object : CountDownTimer(30000000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            chronometerSeconds.value++
        }

        override fun onFinish() {
        }
    }

    fun createGame() {
        clean()

        history.difficulty = QuestionGenerator.getDifficulty()

        nextQuestion()
    }

    fun startChronometer(){
        chronometer.start()
    }

    private fun clean() {
        history = HistoryEntity()
        nextQuestion = 0
        chronometerSeconds.value = 0
        historyQuestion.clear()
    }

    fun checkAnswer(answer: Int) {
        if (historyId.value != 0) // Test ended
            return

        if (answer == currentQuestion.value?.answer) {

            createHistoryQuestion(answer)

            nextQuestion()
        } else {
            if (historyQuestion.size < nextQuestion)
                createHistoryQuestion(answer)
        }
    }

    private fun nextQuestion() {
        if (QuestionGenerator.getQuestionList().size > nextQuestion) {
            currentQuestion.value = QuestionGenerator.getQuestionList()[nextQuestion]
            nextQuestion++
        } else {
            createHistory()
        }
    }

    private fun createHistory() {
        launchDefault {
            chronometer.cancel()
            history.timer = chronometerSeconds.value
            historyId.value = rep.insertHistory(HistoryWithQuestions(history, historyQuestion))
            QuestionGenerator.clean()
        }
    }

    private fun createHistoryQuestion(answer: Int) {
        currentQuestion.value?.let { q ->

            //this avoid to duplicate the question
            if (historyQuestion.size >= nextQuestion)
                return

            // counter
            if (answer == q.answer)
                history.correct++
            else
                history.incorrect++

            val question = QuestionEntity(
                question = "${q.first} ${q.operator.mathOperator()} ${q.second}",
                answer = answer,
                correctAnswer = q.answer
            )

            historyQuestion.add(question)
        }
    }

}




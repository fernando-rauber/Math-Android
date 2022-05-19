package uk.fernando.math.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import uk.fernando.math.model.Question
import uk.fernando.math.util.QuestionGenerator


class GameViewModel : BaseViewModel() {

    private var correctCount = 0
    private var incorrectCount = 0
    private var incorrectHelper = 0
    private var nextQuestion = 0
    private var questionList = emptyList<Question>()

    val currentQuestion: MutableState<Question?> = mutableStateOf(null)

    init {
        QuestionGenerator.setUp(listOf(1, 2), 3, 2, 1)
        questionList = QuestionGenerator.generateQuestions()

        nextQuestion()
    }

    fun checkQuestion(isCorrect: Boolean) {
        if (isCorrect) {
            correctCount++
            incorrectHelper = 0

            nextQuestion()
        } else {
            if (incorrectHelper == 0) {
                incorrectCount++
                incorrectHelper++
            }
        }
    }

    private fun nextQuestion() {

        if (questionList.size > (nextQuestion + 1)) {
            currentQuestion.value = questionList[nextQuestion]
            nextQuestion++
        } else {
            // Show Result
        }
    }

}




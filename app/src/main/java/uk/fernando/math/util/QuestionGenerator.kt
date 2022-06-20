package uk.fernando.math.util

import uk.fernando.math.model.Question
import uk.fernando.math.model.enum.Difficulty
import uk.fernando.math.model.enum.Difficulty.*
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

object QuestionGenerator {

    private var operatorList = listOf<Int>()
    private var quantity = 10
    private var difficulty = 1
    private var multipleChoice = true // Multiple choice

    private var minNumber = 1
    private var maxNumber = 999

    private var player1 = ""
    private var player2 = ""

    private val questionList = mutableListOf<Question>()

    fun generateQuestions(operator: List<Int>, quantity: Int, isMultipleChoice: Boolean, difficulty: Int): Boolean {
        this.operatorList = operator
        this.quantity = quantity
        this.multipleChoice = isMultipleChoice

        setDifficulty(difficulty)

        for (i in 1..quantity) {
            val question = createQuestion(operatorList.random())
            questionList.add(question)
        }

        return true
    }

    fun setPlayers(player1: String, player2: String) {
        this.player1 = player1
        this.player2 = player2
    }

    /**
     * Returns difficulty level 1(easy) 2(medium) 3(hard).
     */
    fun getDifficulty() = difficulty

    /**
     * Returns a list of Questions .
     */
    fun getQuestionList() = questionList

    /**
     * Returns a list of math operators .
     */
    fun getMathOperatorList() = operatorList

    /**
     * Returns player 1 name.
     */
    fun getPlayer1() = player1

    /**
     * Returns player 2 name.
     */
    fun getPlayer2() = player2

    fun clean() {
        questionList.clear()
    }

    /**
     * Returns a question based on the math operator.
     */
    private fun createQuestion(operator: Int): Question {
        return when (getByValue(operator)) {
            ADDITION, SUBTRACTION -> getQuestionPlusMinus(operator)
            DIVISION, MULTIPLICATION -> getQuestionDivTimes(operator)
            PERCENTAGE -> getQuestionPercentage()
            SQUARE -> getQuestionSquareRoot()
            GREATER_THAN, LESSER_THAN -> getQuestionGreaterOrLess(operator)
            else -> getQuestionPlusMinus(operator)
        }
    }

    private fun setDifficulty(difficulty: Int) {
        this.difficulty = difficulty

        when (Difficulty.getByValue(difficulty)) {
            EASY -> {
                minNumber = 1
                maxNumber = 50
            }
            MEDIUM -> {
                minNumber = 10
                maxNumber = 150
            }
            HARD -> {
                minNumber = 100
                maxNumber = 999
            }
            else -> {}
        }
    }

    // For Operators + and -
    private fun getQuestionPlusMinus(operator: Int): Question {
        val first = (minNumber..maxNumber).random()
        val second = (minNumber..maxNumber).random()

        val answer = if (operator == ADDITION.value)
            first.plus(second)
        else
            first.minus(second)

        if (answer <= 0) // avoid negative answers
            return getQuestionPlusMinus(operator)

        return Question(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            answer = answer,
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer) else null
        )
    }

    // For Operators > and <
    private fun getQuestionGreaterOrLess(operator: Int): Question {
        val first = (minNumber..maxNumber).random()
        val second = (minNumber..maxNumber).random()

        if (first == second)
            return getQuestionPlusMinus(operator)

        val answer = if (operator == GREATER_THAN.value)
            first > second
        else
            first < second

        return Question(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            answer = if (answer) 1 else 0,
            multipleChoices =  null
        )
    }

    // For Operators / and *
    private fun getQuestionDivTimes(operator: Int): Question {
        var first = (minNumber..maxNumber / 2).random()
        val second = (minNumber..maxNumber / 3).random()

        val answer = if (operator == DIVISION.value) {
            first = second.times(first) // it won't end up with decimals
            first.div(second)
        } else
            first.times(second)

        if (answer <= 2) // it can generate other answer for multiple choices
            return getQuestionDivTimes(operator)

        return Question(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            answer = answer,
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer) else null
        )
    }

    // For Operators %
    private fun getQuestionPercentage(): Question {
        val first = (1..99).random()
        val second = (minNumber..maxNumber).random()

        val answer = (first.toDouble() / 100) * second

        if (ceil(answer / 3) != floor(answer / 3)) // just for Int numbers
            return getQuestionPercentage()

        return Question(
            value1 = first.toString(),
            operator = 5,
            value2 = second.toString(),
            answer = answer.toInt(),
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer.toInt()) else null
        )
    }

    // For Operators square root
    private fun getQuestionSquareRoot(): Question {
        val max = when (Difficulty.getByValue(difficulty)) {
            EASY -> 27
            MEDIUM -> 60
            else -> 120
        }
        val second = (1..max).random()
        val answer = second.toDouble().pow(2.0)

        return Question(
            value1 = "",
            operator = 6,
            value2 = second.toString(),
            answer = answer.toInt(),
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer.toInt()) else null
        )
    }

    /**
     * Returns a list of random answer based on the original answer.
     */
    private fun generateMultipleChoices(answer: Int): List<Int> {
        val multipleChoice = mutableListOf<Int>()

        multipleChoice.add(answer)

        for (i in 1..3) {
            multipleChoice.add(createFakeAnswer(answer, multipleChoice))
        }

        return multipleChoice
    }

    /**
     * Returns a random answer based the original answer with value around 4 to 25% from the original.
     */
    private fun createFakeAnswer(answer: Int, existentAnswers: List<Int>): Int {
        val maxPercent = if (answer < 7) 90 else 25
        val percentage = (4..maxPercent).random()

        val fakeAnswer = answer.toDouble() / 100 * percentage

        val result = when ((1..2).random()) {
            1 -> answer.minus(fakeAnswer.toInt())
            else -> answer.plus(fakeAnswer.toInt())
        }

        return if (existentAnswers.contains(result)) // can't have the same answer
            createFakeAnswer(answer, existentAnswers)
        else
            result
    }
}

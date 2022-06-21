package uk.fernando.math.util

import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.model.enum.Difficulty
import uk.fernando.math.model.enum.Difficulty.EASY
import uk.fernando.math.model.enum.Difficulty.MEDIUM
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class QuestionGenerator {

    private var difficulty = 1
    private var quantity = 10

    private val squareRootList by lazy {
        listOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }

    private val minPlusMinus by lazy {
        when (Difficulty.getByValue(difficulty)) {
            EASY -> 1
            MEDIUM -> 10
            else -> 100
        }
    }
    private val maxPlusMinus by lazy {
        when (Difficulty.getByValue(difficulty)) {
            EASY -> 50
            MEDIUM -> 150
            else -> 999
        }
    }

    private val minDivTimes by lazy {
        when (Difficulty.getByValue(difficulty)) {
            EASY -> 1
            MEDIUM -> 10
            else -> 15
        }
    }
    private val maxDivTimes by lazy {
        when (Difficulty.getByValue(difficulty)) {
            EASY -> 10
            MEDIUM -> 50
            else -> 99
        }
    }

    private val difficultSquareRootList by lazy {
        when (Difficulty.getByValue(difficulty)) {
            EASY -> squareRootList.take(quantity + 5)
            MEDIUM -> squareRootList.take(40)
            else -> squareRootList.takeLast(quantity + 20)
        }.shuffled().toMutableList()
    }

    fun generateQuestions(operator: List<Int>, quantity: Int, difficulty: Int): List<QuestionEntity> {
        val questionList = mutableListOf<QuestionEntity>()

        this.difficulty = difficulty
        this.quantity = quantity

        for (i in 1..quantity) {
            questionList.add(createQuestion(operator.random()))
        }

        return questionList
    }


    /**
     * Returns a question based on the math operator.
     */
    private fun createQuestion(operator: Int): QuestionEntity {
        return when (getByValue(operator)) {
            ADDITION, SUBTRACTION -> getQuestionPlusMinus(operator)
            DIVISION, MULTIPLICATION -> getQuestionDivTimes(operator)
            PERCENTAGE -> getQuestionPercentage()
            SQUARE -> getQuestionSquareRoot()
            GREATER_THAN, LESSER_THAN -> getQuestionGreaterOrLess(operator)
            else -> getQuestionPlusMinus(operator)
        }
    }

    // For Operators + and -
    private fun getQuestionPlusMinus(operator: Int): QuestionEntity {
        val first = (minPlusMinus..maxPlusMinus).random()
        val second = (minPlusMinus..maxPlusMinus).random()

        val answer = if (operator == ADDITION.value)
            first.plus(second)
        else
            first.minus(second)

        if (answer <= 0) // avoid negative answers
            return getQuestionPlusMinus(operator)

        return QuestionEntity(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            correctAnswer = answer
        )
    }

    // For Operators > and <
    private fun getQuestionGreaterOrLess(operator: Int): QuestionEntity {
        val first = (minPlusMinus..maxPlusMinus).random()
        val second = (minPlusMinus..maxPlusMinus).random()

        if (first == second)
            return getQuestionGreaterOrLess(operator)

        val answer = if (operator == GREATER_THAN.value)
            first > second
        else
            first < second

        return QuestionEntity(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            correctAnswer = if (answer) 1 else 0
        )
    }

    // For Operators / and *
    private fun getQuestionDivTimes(operator: Int): QuestionEntity {
        var first = (minDivTimes..maxDivTimes).random()
        val second = (minDivTimes..maxDivTimes).random()

        val answer = if (operator == DIVISION.value) {
            first = second.times(first) // it won't end up with decimals
            first.div(second)
        } else
            first.times(second)

        if (answer <= 2) // it can generate other answer for multiple choices
            return getQuestionDivTimes(operator)

        return QuestionEntity(
            value1 = first.toString(),
            operator = operator,
            value2 = second.toString(),
            correctAnswer = answer
        )
    }

    // For Operators %
    private fun getQuestionPercentage(): QuestionEntity {
        val first = (1..99).random()
        val second = (minPlusMinus..maxPlusMinus).random()

        val answer = (first.toDouble() / 100) * second

        if (ceil(answer / 3) != floor(answer / 3)) // just for Int numbers
            return getQuestionPercentage()

        return QuestionEntity(
            value1 = first.toString(),
            operator = 5,
            value2 = second.toString(),
            correctAnswer = answer.toInt()
        )
    }

    // For Operators square root
    private fun getQuestionSquareRoot(): QuestionEntity {
        val value = difficultSquareRootList.first()
        difficultSquareRootList.remove(value)
        val answer = sqrt(value.toString().toDouble())

        return QuestionEntity(
            value1 = "",
            operator = 6,
            value2 = value.toString(),
            correctAnswer = answer.toInt()
        )
    }
}

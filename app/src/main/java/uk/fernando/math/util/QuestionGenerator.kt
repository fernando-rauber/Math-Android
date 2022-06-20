package uk.fernando.math.util

import uk.fernando.math.database.entity.QuestionEntity
import uk.fernando.math.model.enum.Difficulty
import uk.fernando.math.model.enum.Difficulty.*
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

//private val squareRootList = listOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)

class QuestionGenerator {

    private var quantity = 10
    private var difficulty = 1

    private var minNumber = 1
    private var maxNumber = 999

    private val difficultSquareRootList = mutableListOf<Int>()

    fun generateQuestions(operator: List<Int>, quantity: Int, difficulty: Int): List<QuestionEntity> {
        val questionList = mutableListOf<QuestionEntity>()

        this.quantity = quantity
        setDifficulty(difficulty)

//        getSquareRootByDifficult()

        for (i in 1..quantity) {
            questionList.add(createQuestion(operator.random()))
        }

        return questionList
    }

    private fun getSquareRootByDifficult() {
//        if (operatorList.contains(SQUARE.value)) {
//            difficultSquareRootList.clear()

//            when (Difficulty.getByValue(difficulty)) {
//                EASY -> difficultSquareRootList.addAll(squareRootList.take(30).shuffled())
//                MEDIUM -> difficultSquareRootList.addAll(squareRootList.shuffled().take(30))
//                HARD -> difficultSquareRootList.addAll(squareRootList.takeLast(30).shuffled())
//                else -> {}
//            }
//        }
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
    private fun getQuestionPlusMinus(operator: Int): QuestionEntity {
        val first = (minNumber..maxNumber).random()
        val second = (minNumber..maxNumber).random()

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
        val first = (minNumber..maxNumber).random()
        val second = (minNumber..maxNumber).random()

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
        var first = (minNumber..maxNumber / 2).random()
        val second = (minNumber..maxNumber / 3).random()

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
        val second = (minNumber..maxNumber).random()

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

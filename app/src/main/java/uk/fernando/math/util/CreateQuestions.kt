package uk.fernando.math.util

import uk.fernando.math.model.Question
import uk.fernando.math.model.enum.MathOperator.*
import uk.fernando.math.model.enum.MathOperator.Companion.getByValue

class CreateQuestions {

    private var operatorList = listOf<Int>()
    private var quantity = 10
    private var multipleChoice = true // Multiple choice

    private var minNumber = 1
    private var maxNumber = 999

    fun setUp(operator: List<Int>, quantity: Int, typeAnswer: Int, difficulty: Int) {
        this.operatorList = operator
        this.quantity = quantity
        this.multipleChoice = typeAnswer == 2

        setDifficulty(difficulty)
    }

    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            1 -> {
                minNumber = 1
                maxNumber = 50
            }
            2 -> {
                minNumber = 10
                maxNumber = 150
            }
            3 -> {
                minNumber = 100
                maxNumber = 999
            }
        }
    }
    /**
     * Returns a list of Question based on the parameters on setUp function.
     */
    fun generateQuestions(): List<Question> {
        val questionList = mutableListOf<Question>()

        for (i in 1..quantity) {
            val question = createQuestion(operatorList.random())

            questionList.add(question)
        }

        return questionList
    }

    /**
     * Returns a question based on the math operator.
     */
    private fun createQuestion(operator: Int): Question {
        return when (getByValue(operator)) {
            ADDITION, SUBTRACTION -> getQuestionPlusMinus(operator)
            DIVISION, MULTIPLICATION -> getQuestionDivTimes(operator)
            PERCENTAGE -> getQuestionPlusMinus(operator)
            SQUARE -> getQuestionPlusMinus(operator)
            else -> getQuestionPlusMinus(operator) // FRACTION
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
            first = first.toString(),
            operator = operator,
            second = second.toString(),
            answer = answer,
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer) else null
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
            first = first.toString(),
            operator = operator,
            second = second.toString(),
            answer = answer,
            multipleChoices = if (multipleChoice) generateMultipleChoices(answer) else null
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
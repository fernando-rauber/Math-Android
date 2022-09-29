package uk.fernando.math.database.entity

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = QuestionEntity.NAME,
    foreignKeys = [ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["player_id"], onDelete = ForeignKey.CASCADE)]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "value_1")
    val value1: String,
    @ColumnInfo(name = "value_2")
    val value2: String,
    val operator: Int,
    var answer: Int? = null,
    val correctAnswer: Int,

    @ColumnInfo(name = "player_id", index = true)
    var playerId: Long = 0,

    ) : Serializable {

    @Ignore
    var multipleChoice = emptyList<Int>()

    @Ignore
    var multipleChoicePlayer2 = emptyList<Int>()

    fun getMultipleChoicePlayerOne(): List<Int> {
        if (multipleChoice.isEmpty())
            multipleChoice = generateMultipleChoices(correctAnswer).shuffled()

        return multipleChoice
    }

    fun getMultipleChoicePlayerTwo(): List<Int> {
        if (multipleChoicePlayer2.isEmpty())
            multipleChoicePlayer2 = getMultipleChoicePlayerOne().shuffled()
        
        return multipleChoicePlayer2
    }

    companion object {
        const val NAME = "question"
    }

    /**
     * Returns a list of random answer based on the original answer.
     */
    private fun generateMultipleChoices(correctAnswer: Int): List<Int> {
        val multipleChoice = mutableListOf<Int>()

        multipleChoice.add(correctAnswer)

        for (i in 1..3) {
            multipleChoice.add(createFakeAnswer(correctAnswer, multipleChoice))
        }

        return multipleChoice
    }

    private fun createFakeAnswer(answer: Int, existentAnswers: List<Int>): Int {
        val value = (1..5).random()

        val result = when ((1..2).random()) {
            1 -> answer.minus(value)
            else -> answer.plus(value)
        }

        return if (result < 0 || existentAnswers.contains(result)) // can't have the same answer
            createFakeAnswer(answer, existentAnswers)
        else
            result
    }
}

package uk.fernando.math.database.entity

import androidx.room.*
import uk.fernando.math.ext.generateMultipleChoices
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

    fun getMultipleChoiceList(): List<Int> {
        if (multipleChoice.isEmpty())
            multipleChoice = correctAnswer.generateMultipleChoices()

        return multipleChoice
    }

    companion object {
        const val NAME = "question"
    }
}

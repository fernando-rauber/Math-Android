package uk.fernando.math.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = QuestionEntity.NAME,
    foreignKeys = [ForeignKey(entity = HistoryEntity::class, parentColumns = ["id"], childColumns = ["history_id"])]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    val question: String,
    val answer: Int,
    val correctAnswer: Int,
    @ColumnInfo(name = "history_id", index = true)
    var historyId: Long = 0

) : Serializable {

    companion object {
        const val NAME = "question"
    }
}

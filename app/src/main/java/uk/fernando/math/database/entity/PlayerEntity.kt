package uk.fernando.math.database.entity

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = PlayerEntity.NAME,
    foreignKeys = [ForeignKey(entity = HistoryEntity::class, parentColumns = ["id"], childColumns = ["history_id"], onDelete = ForeignKey.CASCADE)]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var name: String = "",
    var correct: Int = 0,
    var incorrect: Int = 0,

    @ColumnInfo(name = "history_id", index = true)
    var historyId: Long = 0

) : Serializable {

    @Ignore
    val questionList = mutableListOf<QuestionEntity>()

    companion object {
        const val NAME = "player"
    }
}

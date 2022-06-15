package uk.fernando.math.database.entity.multiplayer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import uk.fernando.math.database.entity.QuestionEntity
import java.io.Serializable

@Entity(
    tableName = PlayerAnswerEntity.NAME,
    foreignKeys = [ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["player_id"]),
        ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["question_id"])]
)
data class PlayerAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    val answer: Int,

    @ColumnInfo(name = "player_id", index = true)
    var playerId: Long = 0,

    @ColumnInfo(name = "question_id", index = true)
    var QuestionId: Long = 0

) : Serializable {

    companion object {
        const val NAME = "player_answer"
    }
}

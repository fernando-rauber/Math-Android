package uk.fernando.math.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = PlayerQuestionEntity.NAME,
    foreignKeys = [ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["player_id"])]
)
data class PlayerQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    val question: String,
    val answer: Int?,
    val correctAnswer: Int,

    @ColumnInfo(name = "player_id", index = true)
    var playerId: Long = 0,

) : Serializable {

    companion object {
        const val NAME = "player_question"
    }
}

package uk.fernando.math.database.entity.multiplayer

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = PlayerEntity.NAME,
    foreignKeys = [ForeignKey(entity = MultiplayerHistoryEntity::class, parentColumns = ["id"], childColumns = ["history_id"])]
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
    val questionList = mutableListOf<PlayerQuestionEntity>()

    companion object {
        const val NAME = "player"
    }
}

package uk.fernando.math.database.entity.multiplayer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = MultiplayerHistoryEntity.NAME)
data class MultiplayerHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val date: Date = Date(),
    var difficulty: Int = 0,
    @ColumnInfo(name = "operators")
    var operatorList: List<Int> = emptyList(),

//    var correct: Int = 0,
//    var incorrect: Int = 0

) : Serializable {

    companion object {
        const val NAME = "multiplayer_history"
    }
}

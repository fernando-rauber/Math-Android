package uk.fernando.math.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = HistoryEntity.NAME)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val date: Date = Date(),
    var timer: Int = 0,
    var difficulty: Int = 0,
    @ColumnInfo(name = "operators")
    var operatorList: List<Int> = emptyList(),
    var multiplayer: Boolean = false

) : Serializable {

    companion object {
        const val NAME = "history"
    }
}

package uk.fernando.math.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = HistoryEntity.NAME)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val date: Date = Date(),
    var difficulty: Int = 0,
    var correct: Int = 0,
    var incorrect: Int = 0

) : Serializable {

    companion object {
        const val NAME = "history"
    }
}

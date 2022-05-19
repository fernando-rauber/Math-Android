package uk.fernando.math.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = HistoryEntity.NAME)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val date: Date ,
    val difficulty: Int,
    val correct: Int,
    val incorrect: Int

) : Serializable {

    companion object {
        const val NAME = "history"
    }
}

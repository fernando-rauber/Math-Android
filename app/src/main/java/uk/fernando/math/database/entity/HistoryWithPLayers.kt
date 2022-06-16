package uk.fernando.math.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


data class HistoryWithPLayers(

    @Embedded val history: HistoryEntity,

    @Relation(parentColumn = "id", entityColumn = "history_id", entity = PlayerEntity::class)
    val playerList: List<PlayerEntity> = listOf()

) : Serializable

fun HistoryWithPLayers.firstPlayer() = if(playerList.isEmpty()) PlayerEntity() else playerList.first()
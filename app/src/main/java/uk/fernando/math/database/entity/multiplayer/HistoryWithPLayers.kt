package uk.fernando.math.database.entity.multiplayer

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


data class HistoryWithPLayers(

    @Embedded val history: MultiplayerHistoryEntity,

    @Relation(parentColumn = "id", entityColumn = "history_id", entity = PlayerEntity::class)
    val playerList: List<PlayerEntity> = listOf()

) : Serializable
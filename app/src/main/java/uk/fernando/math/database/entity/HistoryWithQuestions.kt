package uk.fernando.math.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


data class HistoryWithQuestions(

    @Embedded val history: HistoryEntity,

    @Relation(parentColumn = "id", entityColumn = "history_id", entity = QuestionEntity::class)
    val questionList: List<QuestionEntity>

) : Serializable
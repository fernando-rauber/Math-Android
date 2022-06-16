package uk.fernando.math.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.fernando.math.database.converter.DateTypeConverter
import uk.fernando.math.database.converter.ListTypeConverter
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.PlayerQuestionEntity


@TypeConverters(DateTypeConverter::class, ListTypeConverter::class)
@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [HistoryEntity::class, PlayerEntity::class, PlayerQuestionEntity::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}

const val DATABASE_VERSION = 1

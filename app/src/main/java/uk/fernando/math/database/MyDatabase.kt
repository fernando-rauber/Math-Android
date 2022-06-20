package uk.fernando.math.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.fernando.math.database.converter.DateTypeConverter
import uk.fernando.math.database.converter.ListTypeConverter
import uk.fernando.math.database.dao.GameDao
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.database.entity.HistoryEntity
import uk.fernando.math.database.entity.PlayerEntity
import uk.fernando.math.database.entity.QuestionEntity


@TypeConverters(DateTypeConverter::class, ListTypeConverter::class)
@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [HistoryEntity::class, PlayerEntity::class, QuestionEntity::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun gameDao(): GameDao
}

const val DATABASE_VERSION = 1

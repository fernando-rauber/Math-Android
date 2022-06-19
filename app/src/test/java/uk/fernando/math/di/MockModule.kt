package uk.fernando.math.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import io.mockk.every
import io.mockk.mockk
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.bind
import org.koin.dsl.module
import uk.fernando.math.database.MyDatabase
import uk.fernando.math.database.dao.HistoryDao
import uk.fernando.math.di.KoinModule.allModules
import uk.fernando.math.repository.HistoryRepositoryMock
import uk.fernando.math.viewmodel.solo.GameViewModel

val mockModule = module {
    single { mockk<Application>() }
    single { mockk<Context>() }
    single {
        mockk<SharedPreferences>().also { prefs ->
            every { prefs.getString(any(), any()) } returns null
        }
    }
    single { Room.inMemoryDatabaseBuilder(get(), MyDatabase::class.java) }
    single { KoinModule.getAndroidLogger() }
    factory(qualifier = StringQualifier("common")) { mockk<SharedPreferences>() }

}

val mockedDAOModule = module {
    single<HistoryDao> { HistoryRepositoryMock() } bind HistoryDao::class
}

fun allMockedModules() = allModules() + mockModule + mockedDAOModule
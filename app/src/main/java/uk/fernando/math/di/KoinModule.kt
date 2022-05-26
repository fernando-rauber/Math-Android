package uk.fernando.math.di


import android.app.Application
import android.os.Build
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.fernando.logger.AndroidLogger
import uk.fernando.logger.MyLogger
import uk.fernando.math.BuildConfig
import uk.fernando.math.database.MyDatabase
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.viewmodel.*

object KoinModule {

    /**
     * Keep the order applied
     * @return List<Module>
     */
    fun allModules(): List<Module> =
        listOf(coreModule, databaseModule, repositoryModule, viewModelModule)

    private val coreModule = module {
        single { getAndroidLogger() }
    }

    private val databaseModule = module {

        fun provideDatabase(application: Application): MyDatabase {
            return Room.databaseBuilder(application, MyDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single { provideDatabase(androidApplication()) }
        factory { get<MyDatabase>().historyDao() }
    }

    private val repositoryModule: Module
        get() = module {
            factory { HistoryRepository(get()) }
        }

    private val viewModelModule: Module
        get() = module {

            viewModel { CreateGameViewModel(get()) }
            viewModel { GameViewModel(get(), get()) }
            viewModel { SummaryViewModel(get()) }
            viewModel { HistoryViewModel(get()) }
            viewModel { SettingsViewModel() }
        }

    private const val DB_NAME = "math_fun.db"

    private fun getAndroidLogger(): MyLogger {
        return if (BuildConfig.BUILD_TYPE == "debug")
            AndroidLogger(MyLogger.LogLevel.DEBUG)
        else
            AndroidLogger(MyLogger.LogLevel.ERROR)
    }
}



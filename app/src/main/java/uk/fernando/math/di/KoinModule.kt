package uk.fernando.math.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.fernando.logger.AndroidLogger
import uk.fernando.logger.MyLogger
import uk.fernando.math.BuildConfig
import uk.fernando.math.database.MyDatabase
import uk.fernando.math.datastore.GamePrefsStore
import uk.fernando.math.datastore.GamePrefsStoreImpl
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.datastore.PrefsStoreImpl
import uk.fernando.math.notification.NotificationHelper
import uk.fernando.math.repository.GameRepository
import uk.fernando.math.repository.GameRepositoryImpl
import uk.fernando.math.repository.HistoryRepository
import uk.fernando.math.repository.HistoryRepositoryImpl
import uk.fernando.math.usecase.GamePrefsUseCase
import uk.fernando.math.usecase.PurchaseUseCase
import uk.fernando.math.viewmodel.SettingsViewModel
import uk.fernando.math.viewmodel.SplashViewModel
import uk.fernando.math.viewmodel.multiplayer.MultiplayerCreateGameViewModel
import uk.fernando.math.viewmodel.multiplayer.MultiplayerGameViewModel
import uk.fernando.math.viewmodel.multiplayer.MultiplayerHistoryViewModel
import uk.fernando.math.viewmodel.multiplayer.MultiplayerSummaryViewModel
import uk.fernando.math.viewmodel.solo.CreateGameViewModel
import uk.fernando.math.viewmodel.solo.GameViewModel
import uk.fernando.math.viewmodel.solo.HistoryViewModel
import uk.fernando.math.viewmodel.solo.SummaryViewModel

object KoinModule {

    /**
     * Keep the order applied
     * @return List<Module>
     */
    fun allModules(): List<Module> =
        listOf(coreModule, databaseModule, repositoryModule, useCaseModule, viewModelModule)

    private val coreModule = module {

        single { getAndroidLogger() }
        single<PrefsStore> { PrefsStoreImpl(androidApplication()) }
        single<GamePrefsStore> { GamePrefsStoreImpl(androidApplication()) }
    }

    private val databaseModule = module {

        fun provideDatabase(application: Application): MyDatabase {
            return Room.databaseBuilder(application, MyDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single { provideDatabase(androidApplication()) }
        single { NotificationHelper(androidApplication()) }
        factory { get<MyDatabase>().historyDao() }
        factory { get<MyDatabase>().gameDao() }
    }

    private val repositoryModule: Module
        get() = module {

            factory<HistoryRepository> { HistoryRepositoryImpl(get(), get()) }
            factory<GameRepository> { GameRepositoryImpl(get()) }
        }

    private val useCaseModule: Module
        get() = module {
            single { PurchaseUseCase(get(), get(), get()) }
            single { GamePrefsUseCase(get()) }
        }

    private val viewModelModule: Module
        get() = module {

            viewModel { CreateGameViewModel(get(), get(), get()) }
            viewModel { GameViewModel(get(), get()) }
            viewModel { SummaryViewModel(get()) }
            viewModel { HistoryViewModel(get()) }
            viewModel { MultiplayerHistoryViewModel(get()) }
            viewModel { MultiplayerCreateGameViewModel(get(), get(), get()) }
            viewModel { MultiplayerGameViewModel(get(), get()) }
            viewModel { MultiplayerSummaryViewModel(get()) }
            viewModel { SettingsViewModel(get(), get(), get()) }
            viewModel { SplashViewModel(get(), get()) }
        }

    private const val DB_NAME = "math_fun.db"

    fun getAndroidLogger(): MyLogger {
        return if (BuildConfig.BUILD_TYPE == "debug")
            AndroidLogger(MyLogger.LogLevel.DEBUG)
        else
            AndroidLogger(MyLogger.LogLevel.ERROR)
    }
}



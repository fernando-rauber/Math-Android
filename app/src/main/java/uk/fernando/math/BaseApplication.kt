package uk.fernando.math

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uk.fernando.advertising.MyAdvertising
import uk.fernando.math.di.KoinModule
import uk.fernando.math.util.QuestionGenerator

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        QuestionGenerator.generateQuestions(listOf(1,2), 5, true, 1)

        MyAdvertising.initialize(this)

        startKoin {
            androidContext(this@BaseApplication)
            modules(KoinModule.allModules())
        }
    }
}
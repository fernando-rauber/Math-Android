package uk.fernando.math

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uk.fernando.advertising.MyAdvertising
import uk.fernando.math.di.KoinModule

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

//        AdSettings.addTestDevice("1B8A325EEFF8BEF2134994B7A47F8F19")
//        MyAdvertising.setDeviceID(listOf("1B8A325EEFF8BEF2134994B7A47F8F19"))
        MyAdvertising.initialize(this)

        startKoin {
            androidContext(this@BaseApplication)
            modules(KoinModule.allModules())
        }
    }
}
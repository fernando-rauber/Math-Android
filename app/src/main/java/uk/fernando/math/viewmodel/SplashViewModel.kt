package uk.fernando.math.viewmodel

import uk.fernando.math.datastore.PrefsStore

class SplashViewModel(private val prefs: PrefsStore) : BaseViewModel() {

    fun firstSetUp(isDarkMode: Boolean) {
        launchIO {
            if (prefs.isFirstTime()) {
                prefs.storeDarkMode(isDarkMode)
                prefs.storeFirstTime(false)
            }
        }
    }
}




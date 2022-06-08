package uk.fernando.math.viewmodel

import uk.fernando.math.datastore.PrefsStore


class SettingsViewModel(val prefs: PrefsStore) : BaseViewModel() {

    fun updateDarkMode(isDarkMode: Boolean) {
        launchIO {
            prefs.storeDarkMode(isDarkMode)
        }
    }
}




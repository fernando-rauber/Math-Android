package uk.fernando.math.viewmodel

import uk.fernando.math.datastore.PrefsStore

class SettingsViewModel(val prefs: PrefsStore) : BaseViewModel() {

    fun updateDarkMode(isDarkMode: Boolean) {
        launchIO { prefs.storeDarkMode(isDarkMode) }
    }

    fun updateAllowDecimals(allow: Boolean) {
        launchIO { prefs.storeAllowDecimals(allow) }
    }

    fun updatePremium(isPremium: Boolean) {
        launchIO { prefs.storePremium(isPremium) }
    }

    fun updateNotification(notification: Boolean) {
    }
}




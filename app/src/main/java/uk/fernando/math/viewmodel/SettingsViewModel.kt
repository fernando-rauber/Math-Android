package uk.fernando.math.viewmodel

import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.notification.NotificationHelper

class SettingsViewModel(private val notificationHelper: NotificationHelper, val prefs: PrefsStore) : BaseViewModel() {

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
        launchIO {
            prefs.storeNotification(notification)
            if (notification)
                notificationHelper.startNotification(R.string.notification_title, R.string.notification_text, 1)
            else
                notificationHelper.stopNotification()
        }
    }
}




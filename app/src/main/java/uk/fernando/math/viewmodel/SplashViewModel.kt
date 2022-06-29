package uk.fernando.math.viewmodel

import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.notification.NotificationHelper

class SplashViewModel(private val notificationHelper: NotificationHelper, private val prefs: PrefsStore) : BaseViewModel() {

    fun firstSetUp(isDarkMode: Boolean) {
        launchIO {
            if (prefs.getVersion() == 1) {
                prefs.storeDarkMode(isDarkMode)
                prefs.storeVersion(2)

//                notificationHelper.startNotification(R.string.notification_title, R.string.notification_text)
            }
        }
    }
}
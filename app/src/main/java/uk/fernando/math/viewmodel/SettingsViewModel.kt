package uk.fernando.math.viewmodel

import uk.fernando.logger.MyLogger
import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.TAG
import uk.fernando.math.notification.NotificationHelper

class SettingsViewModel(
    private val notificationHelper: NotificationHelper,
    private val logger: MyLogger,
    val prefs: PrefsStore
) : BaseViewModel() {

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
            kotlin.runCatching {
                prefs.storeNotification(notification)
                if (notification)
                    notificationHelper.startNotification(R.string.notification_title, R.string.notification_text, 36)
                else
                    notificationHelper.stopNotification()
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG,"Error to start/stop notifications: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }
}




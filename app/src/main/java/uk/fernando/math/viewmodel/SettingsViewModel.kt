package uk.fernando.math.viewmodel

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import uk.fernando.billing.BillingHelper
import uk.fernando.billing.BillingState
import uk.fernando.logger.MyLogger
import uk.fernando.math.BuildConfig
import uk.fernando.math.R
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.TAG
import uk.fernando.math.notification.NotificationHelper
import uk.fernando.snackbar.SnackBarSealed

const val PREMIUM_PRODUCT = "fun_math_premium"

class SettingsViewModel(
    private val application: Application,
    private val notificationHelper: NotificationHelper,
    private val logger: MyLogger,
    val prefs: PrefsStore
) : BaseViewModel() {

    val snackBar: MutableState<SnackBarSealed?> = mutableStateOf(null)
    private var billingHelper: BillingHelper? = null
    private var isPremium = false

    fun initialiseBillingHelper(isInternetAvailable: Boolean) {
        if (isInternetAvailable)
            startInAppPurchaseJourney()
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        launchIO { prefs.storeDarkMode(isDarkMode) }
    }

//    fun updateNotification(notification: Boolean) {
//        launchIO {
//            kotlin.runCatching {
//                prefs.storeNotification(notification)
//                if (notification)
//                    notificationHelper.startNotification(R.string.notification_title, R.string.notification_text)
//                else
//                    notificationHelper.stopNotification()
//            }.onFailure { e ->
//                logger.e(TAG, e.message.toString())
//                logger.addMessageToCrashlytics(TAG, "Error to start/stop notifications: msg: ${e.message}")
//                logger.addExceptionToCrashlytics(e)
//            }
//        }
//    }

    private fun startInAppPurchaseJourney() {
        launchIO {
            val isPremium = prefs.isPremium().first()
            this.isPremium = isPremium

            if (!isPremium) {
                billingHelper = BillingHelper.getInstance(
                    application,
                    viewModelScope,
                    arrayOf(PREMIUM_PRODUCT), // one only purchase
                    arrayOf(), // subscription
                    BuildConfig.BILLING_PUBLIC_KEY
                )

                observeBillingState()
            }
        }
    }

    fun requestPayment(activity: Activity, isInternetAvailable: Boolean) {
        if (!isInternetAvailable(isInternetAvailable))
            return

        launchIO {
            if (billingHelper == null) {
                startInAppPurchaseJourney()
                delay(1000)
            }
            billingHelper?.launchBillingFlow(activity, PREMIUM_PRODUCT)
        }
    }

    fun restorePremium(isInternetAvailable: Boolean) {
        if (!isInternetAvailable(isInternetAvailable))
            return

        if (isPremium)
            snackBar.value = SnackBarSealed.Success(R.string.restore_restored)
        else
            launchIO {
                if (billingHelper == null) {
                    startInAppPurchaseJourney()
                    delay(1000)
                }
                val isPurchased = billingHelper?.isPurchased(PREMIUM_PRODUCT)?.distinctUntilChanged()?.first()
                if (isPurchased == true) {
                    prefs.storePremium(true)
                    snackBar.value = SnackBarSealed.Success(R.string.restore_restored)
                } else {
                    snackBar.value = SnackBarSealed.Error(R.string.restore_not_found)
                }
            }
    }

    private fun observeBillingState() {
        launchIO {
            billingHelper?.getBillingState()?.collect { state ->
                when (state) {
                    is BillingState.Error -> {
                        snackBar.value = SnackBarSealed.Error(R.string.purchase_error)
                        logger.e(TAG, state.message)
                        logger.addMessageToCrashlytics(TAG, "Error - Purchase In App: msg: ${state.message}")
                    }
                    is BillingState.Success -> {
                        snackBar.value = SnackBarSealed.Success(R.string.purchase_success, isLongDuration = true)
                        prefs.storePremium(true)
                    }
                    is BillingState.Crashlytics -> {
                        logger.e(TAG, state.message)
                        logger.addMessageToCrashlytics(TAG, "CrashAnalytics - Purchase In App: msg: ${state.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun isInternetAvailable(isInternetAvailable: Boolean): Boolean {
        if (!isInternetAvailable)
            snackBar.value = SnackBarSealed.Error(R.string.internet_required)

        return isInternetAvailable
    }
}




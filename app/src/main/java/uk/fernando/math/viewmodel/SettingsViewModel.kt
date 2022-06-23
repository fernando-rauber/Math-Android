package uk.fernando.math.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import uk.fernando.billing.BillingHelper
import uk.fernando.billing.BillingState
import uk.fernando.logger.MyLogger
import uk.fernando.math.BuildConfig
import uk.fernando.math.R
import uk.fernando.math.component.snackbar.SnackBarSealed
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.ext.TAG
import uk.fernando.math.notification.NotificationHelper

const val PREMIUM_PRODUCT = "funmath_subscription"

class SettingsViewModel(
    private val application: Application,
    private val notificationHelper: NotificationHelper,
    private val logger: MyLogger,
    val prefs: PrefsStore
) : BaseViewModel() {

    var billingHelper: BillingHelper? = null
    val premiumPrice = mutableStateOf<String?>(null)

    init {
        startInAppPurchaseJourney()
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        launchIO { prefs.storeDarkMode(isDarkMode) }
    }

    fun updateNotification(notification: Boolean) {
        launchIO {
            kotlin.runCatching {
                prefs.storeNotification(notification)
                if (notification)
                    notificationHelper.startNotification(R.string.notification_title, R.string.notification_text, 1)
                else
                    notificationHelper.stopNotification()
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error to start/stop notifications: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun startInAppPurchaseJourney() {
        launchIO {
            val isPremium = prefs.isPremium().first()

            if (!isPremium) {
                billingHelper = BillingHelper.getInstance(
                    application,
                    viewModelScope,
                    arrayOf(PREMIUM_PRODUCT), // one only purchase
                    arrayOf(), // subscription
                    BuildConfig.BILLING_PUBLIC_KEY
                )

                getPremiumPrice()

                observeBillingState()
            }
        }
    }

    fun restorePremium() {
        if (premiumPrice.value == null)
            snackBar.value = SnackBarSealed.Success(R.string.restore_restored)
        else
            launchIO {
                val isPurchased = billingHelper?.isPurchased(PREMIUM_PRODUCT)?.distinctUntilChanged()?.first()
                if (isPurchased == true) {
                    prefs.storePremium(true)
                    premiumPrice.value = null
                    snackBar.value = SnackBarSealed.Success(R.string.restore_restored)
                } else {
                    snackBar.value = SnackBarSealed.Error(R.string.restore_not_found)
                }
            }
    }

    private fun getPremiumPrice() {
        launchIO {
            premiumPrice.value = billingHelper?.getProductPrice(PREMIUM_PRODUCT)?.distinctUntilChanged()?.firstOrNull()
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
                        premiumPrice.value = null
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
}




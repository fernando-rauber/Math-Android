package uk.fernando.math.viewmodel

import android.app.Activity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.fernando.math.datastore.PrefsStore
import uk.fernando.math.notification.NotificationHelper
import uk.fernando.math.usecase.PurchaseUseCase
import uk.fernando.math.util.Resource
import uk.fernando.snackbar.SnackBarSealed

class SettingsViewModel(
    private val notificationHelper: NotificationHelper,
    private val useCase: PurchaseUseCase,
    private val prefs: PrefsStore
) : BaseViewModel() {

    private val _snackBar = MutableStateFlow<SnackBarSealed?>(null)
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    val snackBar: StateFlow<SnackBarSealed?>
        get() = _snackBar.asStateFlow()

    init {
        initialiseBillingHelper()
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        launchIO { prefs.storeDarkMode(isDarkMode) }
    }

    fun updateSound(enable: Boolean) {
        launchIO { prefs.storeSound(enable) }
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

    private fun initialiseBillingHelper() {
        useCase.startInAppPurchaseJourney(scope)

        scope.launch {
            useCase.billingState.collect() { state ->

                when (state) {
                    is Resource.Error -> _snackBar.value = SnackBarSealed.Error(state.data)
                    is Resource.Success -> _snackBar.value = SnackBarSealed.Success(state.data)
                    else -> {}
                }
            }
        }
    }

    fun requestPayment(activity: Activity) {
        useCase.requestPayment(activity, scope)
    }

    fun restorePremium() {
        useCase.restorePremium(scope)
    }

    override fun onCleared() {
        scope.cancel()
    }
}




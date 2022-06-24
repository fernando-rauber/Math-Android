package uk.fernando.math.ext

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.navigation.NavController
import uk.fernando.math.R

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return tag.take(23)
    }

fun NavController.safeNav(direction: String) {
    try {
        this.navigate(direction)
    } catch (e: Exception) {
    }
}

fun Int.timerFormat(): String {
    val minutes = this / 60
    return "${minutes.toString().padStart(2, '0')}:${(this % 60).toString().padStart(2, '0')}"
}

fun Int.toFalseTrue(): Int {
    return if (this == 1) R.string.correct_boolean else R.string.incorrect_boolean
}

fun MediaPlayer.playAudio() {
    if (isPlaying) {
        stop()
        prepare()
    }
    start()
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}
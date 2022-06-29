package uk.fernando.math.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

const val requestCode = 112

class NotificationHelper(val context: Context) {

    private val alarmManager: AlarmManager? by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    }
    private val intent: Intent by lazy {
        Intent(context, NotificationReceiver::class.java)
    }

    fun startNotification(title: Int, message: Int) {
        intent.putExtra(titleExtra, context.resources.getString(title))
        intent.putExtra(messageExtra, context.resources.getString(message))

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 19
        calendar[Calendar.MINUTE] = 10

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            AlarmManager.INTERVAL_HALF_DAY,
            pendingIntent
        )
    }

    fun stopNotification() {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        pendingIntent?.let {
            alarmManager?.cancel(pendingIntent)
        }
    }
}
package uk.fernando.math.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.util.*
import java.util.concurrent.TimeUnit

const val requestCode = 112

class NotificationHelper(val context: Context) {

    private val alarmManager: AlarmManager? by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    }
    private val intent: Intent by lazy {
        Intent(context, NotificationReceiver::class.java)
    }

    fun startNotification(title: Int, message: Int, hourDelay: Long) {
        intent.putExtra(titleExtra, context.resources.getString(title))
        intent.putExtra(messageExtra, context.resources.getString(message))

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar: Calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

//        val cur: Calendar = Calendar.getInstance()
//
//        if (cur.after(calendar)) {
//            calendar.add(Calendar.MINUTE, 20)
//        }
        val timeInterval = TimeUnit.MINUTES.toMillis(30)

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + timeInterval,
            timeInterval,
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
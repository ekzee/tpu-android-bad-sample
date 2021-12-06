package ru.tpu.imgur

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ru.tpu.imgur.notification.AlarmReceiver
import ru.tpu.imgur.notification.AlarmWorker
import ru.tpu.imgur.notification.CatsNotifications
import java.util.concurrent.TimeUnit


class NotificationsPresenter(
        private val context: Context,
        widget: Widget,
        private val notificationChannelId: String
) {

    init {
        widget.showNotificationClickListener = {
            showNotification()
        }

        widget.alarmManagerClickListener = {
            toggleAlarmManager()
        }

        widget.workManagerClickListener = {
            toggleWorkManager()
        }
    }

    private fun showNotification() {
        CatsNotifications(context, notificationChannelId).show()
    }

    private fun toggleAlarmManager() {
        val intent = Intent("android.intent.action.NOTIFY")
        intent.setClass(context, AlarmReceiver::class.java)

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, flags)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmAt = System.currentTimeMillis() + 50 * 1000

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmAt, pendingIntent)
    }

    private fun toggleWorkManager() {
        val workManager = WorkManager.getInstance(context)
        val work = OneTimeWorkRequest.Builder(AlarmWorker::class.java)
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build()
        workManager.enqueueUniqueWork(
                "cats",
                ExistingWorkPolicy.REPLACE,
                work
        )
    }

    interface Widget {
        var showNotificationClickListener: (() -> Unit)?
        var alarmManagerClickListener: (() -> Unit)?
        var workManagerClickListener: (() -> Unit)?
    }

    companion object {
        const val ALARM_ID = 1
    }
}
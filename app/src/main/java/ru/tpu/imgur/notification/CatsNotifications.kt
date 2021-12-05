package ru.tpu.imgur.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.tpu.imgur.R

class CatsNotifications(
    private val context: Context,
    private val channelId: String
) {

    companion object {
        const val NOTIFICATION_ID = 0
    }

    fun show() {
        val contentText = "Тестовый текст! Очень и очень длинный, точно не влезет в одну строку..."

        val style = NotificationCompat.BigTextStyle()
            .bigText(contentText)

        val contentIntent = Intent(Settings.ACTION_SETTINGS)
        contentIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            flags
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Время посмотреть котиков!")
            .setContentText(contentText)
            .setStyle(style)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_ID, builder.build())
    }
}
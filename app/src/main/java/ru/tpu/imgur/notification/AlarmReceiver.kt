package ru.tpu.imgur.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.tpu.imgur.App

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        CatsNotifications(context, App.scope.commonChannelId).show()
    }
}
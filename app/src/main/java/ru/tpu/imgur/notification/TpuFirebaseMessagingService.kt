package ru.tpu.imgur.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.tpu.imgur.App

class TpuFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        App.scope.pushTokenRepository.updateToken(token)
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        Log.d("push", "new message! data: ${msg.data}")
        CatsNotifications(App.scope.app, App.scope.commonChannelId).show()
    }
}
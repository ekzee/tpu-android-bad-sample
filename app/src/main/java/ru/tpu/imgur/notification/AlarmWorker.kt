package ru.tpu.imgur.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.tpu.imgur.App

class AlarmWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        CatsNotifications(context, App.scope.commonChannelId).show()

        return Result.success()
    }
}
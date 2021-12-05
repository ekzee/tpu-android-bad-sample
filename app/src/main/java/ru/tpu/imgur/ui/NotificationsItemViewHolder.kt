package ru.tpu.imgur.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.tpu.imgur.R

class NotificationsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val showNotificationBtn = itemView.findViewById<View>(R.id.btn_notification_show)
    val alarmManagerBtn = itemView.findViewById<View>(R.id.btn_notification_alarm_manager)
    val workManagerBtn = itemView.findViewById<View>(R.id.btn_notification_work_manager)
}
package br.com.alarm.app.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.alarm.app.domain.service.NotificationService.Companion.NOTIFICATION_DEFAULT_ID
import br.com.alarm.app.domain.service.NotificationService.Companion.NOTIFICATION_EXTRA

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.getLongExtra(NOTIFICATION_EXTRA, NOTIFICATION_DEFAULT_ID)
    }
}
package br.com.alarm.app.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.alarm.app.domain.service.NotificationService.Companion.NOTIFICATION_DEFAULT_ID
import br.com.alarm.app.domain.service.NotificationService.Companion.NOTIFICATION_EXTRA

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val data = intent?.getLongExtra(NOTIFICATION_EXTRA, NOTIFICATION_DEFAULT_ID)
        Log.e("test", "chegou no receiver: $data")
    }
}
package br.com.alarm.app.domain.alarmreceiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import br.com.alarm.app.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Exibir notificação urgente
        showNotification(context)

        // Tocar um som de alarme
        playAlarmSound(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configuração da notificação
        val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarme!")
            .setContentText("É hora de acordar!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)

        // Exibir a notificação
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun playAlarmSound(context: Context) {
        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
        ringtone.play()
    }
}
package br.com.alarm.app.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import br.com.alarm.app.domain.models.alarm.AlarmItem
import com.google.gson.Gson

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val notificationService = NotificationService(context)
        notificationService.showNotification(AlarmItem())

        intent?.getStringExtra(NotificationService.NOTIFICATION_EXTRA)?.let { stringData ->
            Log.e("test", "ALARME: $stringData")
            val alarm = Gson().fromJson(stringData, AlarmItem::class.java)
            val ringtone = alarm.ringtone
            playAlarmSound(context, Uri.parse(ringtone))
        }
    }

    private fun playAlarmSound(context: Context, alarmUri: Uri) {
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(context, alarmUri)
            isLooping = true // Se quiser que o som do alarme continue tocando até ser parado
            prepare()
            start()
        }


        val stopAlarmAfterSeconds = 30_000L
        android.os.Handler(context.mainLooper).postDelayed({
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
        }, stopAlarmAfterSeconds)
    }
}
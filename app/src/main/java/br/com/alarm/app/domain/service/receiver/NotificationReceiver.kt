package br.com.alarm.app.domain.service.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.service.media_player.MediaPlayerContract
import br.com.alarm.app.domain.service.notification.NotificationService
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var mediaPlayer: MediaPlayerContract

    private var notificationService: NotificationService? = null

    override fun onReceive(context: Context, intent: Intent?) {
        if (notificationService == null) notificationService = NotificationService(context)

        intent?.let {
            when (it.action) {
                NotificationService.NOTIFICATION_ACTION_PLAY -> {
                    showNotification()
                    mediaPlayer.init(context, getRingtoneUri(it, context))
                }

                NotificationService.NOTIFICATION_ACTION_STOP -> {
                    mediaPlayer.stop()
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancelAll()
                }
            }
        }
    }

    private fun showNotification() {
        notificationService?.showNotification(AlarmItem())
    }

    private fun getRingtoneUri(intent: Intent, context: Context): Uri {
        intent.getStringExtra(NotificationService.NOTIFICATION_EXTRA)?.let { stringData ->
            val alarm = Gson().fromJson(stringData, AlarmItem::class.java)
            val ringtone = alarm.ringtone
            return Uri.parse(ringtone)
        }

        return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
    }
}
package br.com.alarm.app.domain.service.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.core.app.NotificationCompat
import br.com.alarm.app.BuildConfig
import br.com.alarm.app.R
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.service.receiver.NotificationReceiver
import br.com.alarm.app.host.HostActivity
import br.com.alarm.app.util.getDate
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject


class NotificationService @Inject constructor(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "${BuildConfig.VERSION_NAME}.NotificationChannel"
        const val NOTIFICATION_EXTRA = "NOTIFICATION_INTENT_DATA"
        const val NOTIFICATION_DEFAULT_ID = -200L
        const val NOTIFICATION_ACTION_STOP = "NotificationService.actionStop"
        const val NOTIFICATION_ACTION_PLAY = "NotificationService.actionPlay"

        fun createNotificationChannel(context: Context) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null, null)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    fun showNotification(alarm: AlarmItem) {
        @ColorInt val color = (-0xff6f85ff).toInt()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_icon_splash)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(buildPendingIntent(alarm))
            .addAction(R.drawable.ic_alarm, context.getString(R.string.stop), actionPendingIntent(alarm))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildCustomLayout(alarm))
            .setLights(color, 500, 500)
            .setAutoCancel(true)
            .setSound(null)
            .build()

        builder.color = Color.BLUE

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder)
    }

    private fun buildPendingIntent(alarm: AlarmItem): PendingIntent {
        val intent = Intent(context, HostActivity::class.java)
        intent.putExtra(NOTIFICATION_EXTRA, alarm.id)
        intent.action = NOTIFICATION_ACTION_STOP
        return PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun actionPendingIntent(alarm: AlarmItem): PendingIntent {
        val buttonIntent = Intent(context, NotificationReceiver::class.java)
        buttonIntent.putExtra(NOTIFICATION_EXTRA, alarm.id)
        buttonIntent.action = NOTIFICATION_ACTION_STOP
        return PendingIntent.getBroadcast(
            context,
            2,
            buttonIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildCustomLayout(alarm: AlarmItem): RemoteViews {
        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_small)

        val notificationMessage =
            context.getString(R.string.notification_hour, alarm.date.getDate(alarm.is24HourFormat))
        notificationLayout.setTextViewText(R.id.tvHourNotification, notificationMessage)

        return notificationLayout
    }

    fun scheduleAlarm(alarm: AlarmItem) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val data = Gson().toJson(alarm)
        intent.putExtra(NOTIFICATION_EXTRA, data)
        intent.action = NOTIFICATION_ACTION_PLAY

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            123,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun getTime(): Long {
        val localDateTime = LocalDateTime.now().plus(3, ChronoUnit.SECONDS)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
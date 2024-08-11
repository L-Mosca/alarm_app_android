package br.com.alarm.app.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.core.app.NotificationCompat
import br.com.alarm.app.R
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.host.HostActivity
import br.com.alarm.app.util.getDate
import javax.inject.Inject


class NotificationService @Inject constructor(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_EXTRA = "NOTIFICATION_INTENT_DATA"
        const val NOTIFICATION_DEFAULT_ID = -200L

        fun createNotificationChannel(context: Context) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
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
            .build()

        builder.color = Color.BLUE

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder)
    }

    private fun buildPendingIntent(alarm: AlarmItem): PendingIntent {
        val intent = Intent(context, HostActivity::class.java)
        intent.putExtra(NOTIFICATION_EXTRA, alarm.id)
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

}
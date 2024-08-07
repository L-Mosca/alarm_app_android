package br.com.alarm.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import br.com.alarm.app.domain.models.alarm.Day
import java.util.Calendar

fun executeDelayed(delay: Long = 200L, runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        runnable.invoke()
    }, delay)
}

fun List<String>.removeBrackets() = this.toString().trim().replace("[", "").replace("]", "")

fun List<Day>.getResumeWeekDays() = this.filter { it.isEnable }.map { it.dayName.substring(0, 3) }


/**
 * Calculate difference between actual time (hour and minute) and time picker time (hour and minute)
 *
 * @return Pair(Hour, Minute) -> Difference between actual time and time picker time
 */
fun Pair<Int, Int>.getDifferenceTime(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val tpHour = this.first
    val tpMinute = this.second

    val currentTimeInMinutes = currentHour * 60 + currentMinute
    var tpTimeInMinutes = tpHour * 60 + tpMinute

    if (tpTimeInMinutes < currentTimeInMinutes) {
        tpTimeInMinutes += 24 * 60
    }

    val timeDifferenceInMinutes = tpTimeInMinutes - currentTimeInMinutes

    val differenceHours = timeDifferenceInMinutes / 60
    val differenceMinutes = timeDifferenceInMinutes % 60

    return Pair(differenceHours, differenceMinutes)
}

@SuppressLint("DefaultLocale")
fun formatHour(hour: Int, minute: Int): String {
    return String.format("%02d:%02d", hour, minute)
}

/**
 * Get next 6 AM to create a default alarm item
 * @see br.com.alarm.app.domain.alarm.AlarmItem.buildDefaultAlarm
 */
fun getNextSixAM(): Long {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    // Set the time to 6 AM
    calendar.set(Calendar.HOUR_OF_DAY, 6)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // If it's already past 6 AM today, move to the next day
    if (currentHour >= 6) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return calendar.timeInMillis
}

fun extractHoursAndMinutesFromTimestamp(timestamp: Long): Pair<Int, Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    return Pair(hours, minutes)
}

fun Uri?.getRingToneTitle(context: Context): String {
    val ringtone = RingtoneManager.getRingtone(context, this)
    return ringtone.getTitle(context)
}

fun Long.updateHoursAndMinutes(hours: Int, minutes: Int): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@updateHoursAndMinutes
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}
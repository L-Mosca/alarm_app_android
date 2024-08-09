package br.com.alarm.app.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

/**
 * Get next 6 AM to create a default alarm item
 * @see br.com.alarm.app.domain.models.alarm.AlarmItem
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

@SuppressLint("DefaultLocale")
fun getHourIn24Format(hour: Int, minute: Int): String {
    return String.format("%02d:%02d", hour, minute)
}

fun extractHoursAndMinutesFromTimestamp(timestamp: Long): Pair<Int, Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    return Pair(hours, minutes)
}

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

fun Long.getHourIn12Format(): String {
    val date = Date(this)
    val format = SimpleDateFormat("hh:mm", Locale.getDefault())
    return format.format(date)
}

fun Long.getDate(is24HourFormat: Boolean): String {
    val (hour, minute) = extractHoursAndMinutesFromTimestamp(this)
    return if (is24HourFormat)
        getHourIn24Format(hour, minute)
    else
        this.getHourIn12Format()
}

fun Long.get12HourFormatTag(): String {
    val (hour, _) = extractHoursAndMinutesFromTimestamp(this)
    return if (hour < 12) "AM" else "PM"
}
package br.com.alarm.app.domain.models.alarm

import android.net.Uri
import android.os.Parcelable
import br.com.alarm.app.util.updateHoursAndMinutes
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Parcelize
data class AlarmItem(
    val id: Int? = null,
    var date: Long? = null,
    var isEnable: Boolean = false,
    var ringtone: Uri? = null,
    var snoozeIsEnabled: Boolean = false,
    var weekDays: WeekDays? = null
) : Parcelable

fun AlarmItem.updateAlarmValue(ringtone: Uri): AlarmItem {
    this.ringtone = ringtone
    return this
}

fun AlarmItem.updateAlarmValue(dayList: List<Day>): AlarmItem {
    this.weekDays?.days = dayList
    return this
}

fun AlarmItem.updateAlarmValue(hour: Int, minute: Int): AlarmItem {
    this.date = this.date?.updateHoursAndMinutes(hour, minute)
    return this
}

@Parcelize
data class WeekDays(
    var days: List<Day>
) : Parcelable {
    companion object {
        fun buildWeekDaysList(): WeekDays {
            val locale = Locale.getDefault()
            val daysOfWeek = getDaysOfWeek(locale)

            return WeekDays(
                daysOfWeek.mapIndexed { index, dayName ->
                    Day(id = index, dayName = dayName, isEnable = true)
                }
            )
        }

        private fun getDaysOfWeek(locale: Locale): List<String> {
            val calendar = Calendar.getInstance(locale)
            val dateFormat = SimpleDateFormat("EEEE", locale)

            val daysOfWeek = mutableListOf<String>()
            for (i in 0 until 7) {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek + i)
                daysOfWeek.add(dateFormat.format(calendar.time))
            }
            return daysOfWeek
        }
    }
}

@Parcelize
data class Day(
    val id: Int,
    val dayName: String,
    var isEnable: Boolean = false
) : Parcelable


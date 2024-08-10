package br.com.alarm.app.domain.models.alarm

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.alarm.app.R
import br.com.alarm.app.domain.database.Converters
import br.com.alarm.app.util.removeBrackets
import br.com.alarm.app.util.updateHoursAndMinutes
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity
data class AlarmItem(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo("date") var date: Long = Date().time,
    @ColumnInfo("is_24_format") var is24HourFormat: Boolean = true,
    @ColumnInfo("is_enable") var isEnable: Boolean = true,
    @ColumnInfo("snooze_is_enable") var snoozeIsEnabled: Boolean = false,
    @ColumnInfo("ringtone") @TypeConverters(Converters::class) var ringtone: Uri? = null,
    @ColumnInfo("week_days") @TypeConverters(Converters::class) var weekDays: WeekDays? = null
) {
    fun copy(): AlarmItem {
        return AlarmItem(
            id = id,
            date = date,
            is24HourFormat = is24HourFormat,
            isEnable = isEnable,
            snoozeIsEnabled = snoozeIsEnabled,
            ringtone = ringtone,
            weekDays = weekDays?.copy()
        )
    }
}

fun AlarmItem?.isEquals(alarm: AlarmItem?): Boolean {
    if (alarm == null) return true

    val sameId = this?.id == alarm.id
    val sameDate = this?.date == alarm.date
    val sameStatus = this?.isEnable == alarm.isEnable
    val sameSnoozeStatus = this?.snoozeIsEnabled == alarm.snoozeIsEnabled
    val sameRingtone = this?.ringtone == alarm.ringtone
    val sameWeekDay = this?.weekDays.isEquals(alarm.weekDays)

    return sameId && sameDate && sameStatus && sameSnoozeStatus && sameRingtone && sameWeekDay
}

fun AlarmItem.updateAlarmValue(ringtone: Uri): AlarmItem {
    this.ringtone = ringtone
    return this
}

fun AlarmItem.updateAlarmValue(dayList: List<Day>): AlarmItem {
    this.weekDays?.days = dayList
    return this
}

fun AlarmItem.updateAlarmValue(hour: Int, minute: Int): AlarmItem {
    this.date = this.date.updateHoursAndMinutes(hour, minute)
    return this
}

fun AlarmItem.updateAlarmValue(isEnabled: Boolean): AlarmItem {
    this.isEnable = isEnabled
    return this
}

@Parcelize
data class WeekDays(
    var days: List<Day>
) : Parcelable {

    fun copy(): WeekDays {
        val list = days.toMutableList()
        list.forEachIndexed { index, day ->
            list[index] = day.copy()
        }

        return WeekDays(days = list)
    }

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

fun WeekDays?.isEquals(weekDays: WeekDays?): Boolean {
    if (weekDays == null) return true
    val list1 = this?.days ?: emptyList()
    if (list1.isEmpty()) return true
    val list2 = weekDays.days

    var isEquals = true

    list1.forEachIndexed { index, day ->
        Log.e("test", "day1: ${day.isEnable}")
        Log.e("test", "day2: ${list2[index].isEnable}")
        Log.e("test", "-----------------------")

        if (day.isEnable != list2[index].isEnable) {
            isEquals = false
        }
    }
    return isEquals
}

fun WeekDays?.getWeekDays(context: Context): String {
    this?.let { weekDays ->
        val days = weekDays.days.filter { it.isEnable }

        return when (days.size) {
            0 -> context.getString(R.string.never)
            7 -> context.getString(R.string.all_week_days)
            else -> days.map { it.dayName.substring(0, 3) }.removeBrackets().plus(".")
        }
    }

    return ""
}

@Parcelize
data class Day(
    val id: Int,
    val dayName: String,
    var isEnable: Boolean = true
) : Parcelable {

    fun copy(): Day {
        return Day(id = id, dayName = dayName, isEnable = isEnable)
    }
}


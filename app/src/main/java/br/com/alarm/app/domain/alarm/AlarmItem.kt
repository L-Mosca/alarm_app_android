package br.com.alarm.app.domain.alarm

import android.content.Context
import android.os.Parcelable
import br.com.alarm.app.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlarmItem(
    val id: Int,
    var date: Long,
    var isEnable: Boolean,
    var soundName: String = "",
    var soundVolume: Int = 0,
    var snoozeIsEnabled: Boolean = false
) : Parcelable


@Parcelize
data class WeekDays(
    var days: List<Day>
) : Parcelable {
    companion object {
        fun buildWeekDaysList(context: Context): WeekDays {
            return WeekDays(
                listOf(
                    Day(id = 0, dayName = context.getString(R.string.sunday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.monday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.tuesday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.wednesday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.thursday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.friday), isEnable = false),
                    Day(id = 0, dayName = context.getString(R.string.saturday), isEnable = false),
                )
            )
        }
    }
}

@Parcelize
data class Day(
    val id: Int,
    val dayName: String,
    var isEnable: Boolean = false
) : Parcelable


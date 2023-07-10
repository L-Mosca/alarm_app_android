package br.com.alarm.app.domain.alarm

import android.os.Parcelable
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
) : Parcelable

@Parcelize
data class Day(
    val id: Int,
    val dayName: String,
    var isEnable: Boolean = false
) : Parcelable


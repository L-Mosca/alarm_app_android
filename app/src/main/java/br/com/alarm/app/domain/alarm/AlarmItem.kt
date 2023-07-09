package br.com.alarm.app.domain.alarm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlarmItem(
    val id: Int,
    val date: Long,
    var isEnable: Boolean,
    val weekDays: List<WeekDay>
) : Parcelable


@Parcelize
data class WeekDay(
    val id: Int,
    val weekDay: String,
    var isEnabled: Boolean
) : Parcelable
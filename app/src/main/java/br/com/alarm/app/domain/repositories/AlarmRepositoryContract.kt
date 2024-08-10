package br.com.alarm.app.domain.repositories

import br.com.alarm.app.domain.models.alarm.AlarmItem

interface AlarmRepositoryContract {

    suspend fun buildDefaultAlarm(): AlarmItem

    suspend fun createAlarm(alarmItem: AlarmItem): Long?

    suspend fun fetchAlarmDetail(alarmId: Long): AlarmItem

    suspend fun fetchAlarmList(): List<AlarmItem>

    suspend fun deleteAlarm(id: Long)

    suspend fun updateAlarm(alarm: AlarmItem)
}
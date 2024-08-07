package br.com.alarm.app.domain.repositories

import br.com.alarm.app.domain.models.alarm.AlarmItem

interface AlarmRepositoryContract {

    suspend fun buildDefaultAlarm() : AlarmItem
}
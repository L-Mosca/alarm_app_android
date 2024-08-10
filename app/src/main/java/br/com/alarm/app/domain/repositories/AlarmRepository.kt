package br.com.alarm.app.domain.repositories

import br.com.alarm.app.domain.database.AlarmDatabaseDAO
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.WeekDays
import br.com.alarm.app.util.getNextSixAM
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import javax.inject.Inject

class AlarmRepository @Inject constructor(
    private val database: AlarmDatabaseDAO,
    private val ringtoneHelper: RingtoneHelperContract
) : AlarmRepositoryContract {

    override suspend fun buildDefaultAlarm(): AlarmItem {
        val defaultRingtone = ringtoneHelper.getDefaultRingtone()
        val alarm =
            AlarmItem(
                date = getNextSixAM(),
                isEnable = true,
                ringtone = defaultRingtone,
                snoozeIsEnabled = false,
                weekDays = WeekDays.buildWeekDaysList()
            )
        return alarm
    }

    override suspend fun createAlarm(alarmItem: AlarmItem): Long? {
        return database.insertAlarm(alarmItem)
    }

    override suspend fun fetchAlarmDetail(alarmId: Long): AlarmItem {
        return database.getAlarmDetail(alarmId)
    }

    override suspend fun fetchAlarmList(): List<AlarmItem> {
        return database.getAlarmList()
    }

    override suspend fun deleteAlarm(id: Long) {
        database.deleteAlarm(id)
    }

    override suspend fun updateAlarm(alarm: AlarmItem) {
        database.updateAlarm(alarm)
    }
}
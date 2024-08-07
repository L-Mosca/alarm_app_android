package br.com.alarm.app.domain.repositories

import android.content.Context
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.WeekDays
import br.com.alarm.app.util.getNextSixAM
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ringtoneHelper: RingtoneHelperContract
) :
    AlarmRepositoryContract {

    override suspend fun buildDefaultAlarm(): AlarmItem {
        val defaultRingtone = ringtoneHelper.getDefaultRingtone()
        val alarm =
            AlarmItem(
                id = null,
                date = getNextSixAM(),
                isEnable = true,
                ringtone = defaultRingtone,
                snoozeIsEnabled = false,
                weekDays = WeekDays.buildWeekDaysList()
            )
        return alarm
    }
}
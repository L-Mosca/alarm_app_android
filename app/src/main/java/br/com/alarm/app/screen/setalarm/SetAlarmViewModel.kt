package br.com.alarm.app.screen.setalarm

import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.Day
import br.com.alarm.app.domain.models.alarm.updateAlarmValue
import br.com.alarm.app.domain.repositories.AlarmRepositoryContract
import br.com.alarm.app.util.removeBrackets
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class SetAlarmViewModel @Inject constructor(
    private val ringtoneHelper: RingtoneHelperContract,
    private val alarmRepository: AlarmRepositoryContract,
) : BaseViewModel() {

    val allDaysSelected = MutableLiveData<Int>()
    val noneDaySelected = MutableLiveData<Unit>()
    val selectedDays = MutableLiveData<String>()
    val saveSuccess = MutableLiveData<Unit>()
    val fetchRingtone = MutableLiveData<Intent>()
    val alarmItem = MutableLiveData<AlarmItem>()

    private var firstAlarmSetup: AlarmItem? = null
    private var isNewAlarm = true

    fun setInitialData(alarmId: Long) {
        // TODO adjust all days selected
        // TODO create new alarm with this screen
        viewModelScope.launch {
            if (alarmId == -100L) {
                val newData = alarmRepository.buildDefaultAlarm()
                alarmItem.postValue(newData)
                allDaysSelected.postValue(R.string.all_week_days)
            } else {
                val alarmDetail = alarmRepository.fetchAlarmDetail(alarmId)
                alarmItem.postValue(alarmDetail)
                isNewAlarm = false
                firstAlarmSetup = alarmDetail
            }
        }
    }

    fun setSelectedDays(dayList: List<Day>) {
        alarmItem.postValue(alarmItem.value?.updateAlarmValue(dayList))

        val daysSelected = dayList.filter { it.isEnable }.map { it.dayName.substring(0, 3) }
        when (daysSelected.size) {
            0 -> noneDaySelected.postValue(Unit)
            7 -> allDaysSelected.postValue(R.string.all_week_days)
            else -> selectedDays.postValue(daysSelected.removeBrackets())
        }
    }

    fun saveClicked() {
        saveSuccess.postValue(Unit)
    }

    fun selectRingtone() {
        fetchRingtone.postValue(ringtoneHelper.buildRingtoneIntent(alarmItem.value?.ringtone))
    }

    fun handleRingtoneSelected(result: ActivityResult) {
        val intent: Intent? = result.data
        val selectedToneUri =
            intent?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        if (selectedToneUri != null) {
            alarmItem.postValue(alarmItem.value?.updateAlarmValue(selectedToneUri))
        }
    }

    fun updateAlarmTime(hour: Int, minute: Int) {
        alarmItem.postValue(alarmItem.value?.updateAlarmValue(hour, minute))
    }

    fun updateAlarmStatus(isEnabled: Boolean) {
        alarmItem.postValue(alarmItem.value?.updateAlarmValue(isEnabled))
    }
}
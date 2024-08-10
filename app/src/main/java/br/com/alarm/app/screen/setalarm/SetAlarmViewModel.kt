package br.com.alarm.app.screen.setalarm

import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.base.SingleLiveData
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.Day
import br.com.alarm.app.domain.models.alarm.updateAlarmValue
import br.com.alarm.app.domain.repositories.AlarmRepositoryContract
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

    val saveSuccess = MutableLiveData<Unit>()
    val fetchRingtone = MutableLiveData<Intent>()
    val alarmItem = MutableLiveData<AlarmItem>()
    val deleteSuccess = SingleLiveData<Unit>()

    private var firstAlarmSetup: AlarmItem? = null
    private var isNewAlarm = true

    fun setInitialData(alarmId: Long) {
        viewModelScope.launch {
            if (alarmId == -100L) {
                val newData = alarmRepository.buildDefaultAlarm()
                alarmItem.postValue(newData)
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
    }

    fun saveClicked() {
        viewModelScope.launch {
            alarmRepository.createAlarm(alarmItem.value!!)
            saveSuccess.postValue(Unit)
        }
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

    fun deleteAlarm() {
        viewModelScope.launch {
            alarmItem.value?.id?.let { alarmRepository.deleteAlarm(it) }
            deleteSuccess.postValue(Unit)
        }
    }
}
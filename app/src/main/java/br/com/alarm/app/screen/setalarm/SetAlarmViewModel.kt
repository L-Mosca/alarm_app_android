package br.com.alarm.app.screen.setalarm

import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.domain.alarm.AlarmItem
import br.com.alarm.app.domain.alarm.Day
import br.com.alarm.app.util.removeBrackets
import br.com.alarm.app.util.ringtone_helper.RingtoneHelperContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class SetAlarmViewModel @Inject constructor(private val ringtoneHelper: RingtoneHelperContract) :
    BaseViewModel() {

    val allDaysSelected = MutableLiveData<Int>()
    val noneDaySelected = MutableLiveData<Unit>()
    val selectedDays = MutableLiveData<String>()
    val saveSuccess = MutableLiveData<Unit>()

    val fetchRingtone = MutableLiveData<Intent>()
    val ringtoneTitle = MutableLiveData<String>()

    val alarmItem = MutableLiveData<AlarmItem>()

    fun setInitialData(alarm: AlarmItem? = null) {
        alarm?.let { alarmItem.postValue(it) }
    }

    fun setSelectedDays(dayList: List<Day>) {
        val daysSelected = dayList.filter { it.isEnable }.map { it.dayName.substring(0, 3) }
        when (daysSelected.size) {
            0 -> noneDaySelected.postValue(Unit)
            7 -> allDaysSelected.postValue(R.string.all_week_days)
            else -> {
                selectedDays.postValue(daysSelected.removeBrackets())
            }
        }
    }

    fun saveClicked() {
        saveSuccess.postValue(Unit)
    }

    fun selectRingtone(currentRingtone: Uri? = null) {
        fetchRingtone.postValue(ringtoneHelper.buildRingtoneIntent(currentRingtone))
    }

    fun handleRingtoneSelected(result: ActivityResult) {
        val intent: Intent? = result.data
        val selectedToneUri =
            intent?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        if (selectedToneUri != null) {
            val title = ringtoneHelper.getRingToneTitle(selectedToneUri)
            ringtoneTitle.postValue(title)
        }
    }

}
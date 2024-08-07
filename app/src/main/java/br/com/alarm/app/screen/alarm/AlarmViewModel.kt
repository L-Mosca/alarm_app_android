package br.com.alarm.app.screen.alarm

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.base.SingleLiveData
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.repositories.AlarmRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val alarmRepository: AlarmRepositoryContract) :
    BaseViewModel() {

    val goToEditScreen = SingleLiveData<AlarmItem>()
    val deleteAlarm = SingleLiveData<Pair<Int, AlarmItem>>()

    val alarmList = MutableLiveData<List<AlarmItem>>()

    fun fetchAlarms() {
        viewModelScope.launch {
            val list = alarmRepository.fetchAlarmList()
            alarmList.postValue(list)
        }
    }

    fun handlePopMenuClick(menuItem: MenuItem, position: Int, alarm: AlarmItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuDeleteAlarm -> {
                deleteAlarm(alarm, position)
                true
            }

            R.id.menuEditAlarm -> {
                goToEditScreen.postValue(alarm)
                true
            }

            else -> false
        }
    }

    private fun deleteAlarm(alarm: AlarmItem, position: Int) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm.id!!)
            deleteAlarm.postValue(Pair(position, alarm))
        }
    }
}
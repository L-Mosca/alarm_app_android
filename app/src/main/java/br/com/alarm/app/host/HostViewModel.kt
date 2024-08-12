package br.com.alarm.app.host

import androidx.lifecycle.ViewModel
import br.com.alarm.app.base.SingleLiveData
import br.com.alarm.app.domain.models.alarm.AlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor() : ViewModel() {

    val createNotification = SingleLiveData<AlarmItem>()
    val scheduleAlarm = SingleLiveData<AlarmItem>()


    fun createNotification(alarm: AlarmItem) {
        createNotification.postValue(alarm)
    }

    fun scheduleAlarm(alarm: AlarmItem) {
        scheduleAlarm.postValue(alarm)
    }

}
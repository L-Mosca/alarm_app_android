package br.com.alarm.app.screen.alarm

import android.view.MenuItem
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.base.SingleLiveData
import br.com.alarm.app.domain.alarm.AlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor() : BaseViewModel() {

    val goToEditScreen = SingleLiveData<AlarmItem>()
    val deleteAlarm = SingleLiveData<Int>()

    fun handlePopMenuClick(menuItem: MenuItem, position: Int, alarm: AlarmItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuDeleteAlarm -> {
                deleteAlarm.postValue(position)
                true
            }

            R.id.menuEditAlarm -> {
                goToEditScreen.postValue(alarm)
                true
            }

            else -> false
        }
    }
}
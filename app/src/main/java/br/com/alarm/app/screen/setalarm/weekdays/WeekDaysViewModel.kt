package br.com.alarm.app.screen.setalarm.weekdays

import androidx.lifecycle.MutableLiveData
import br.com.alarm.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeekDaysViewModel @Inject constructor() : BaseViewModel() {

    val saveClicked = MutableLiveData<Unit>()

    fun onSaveClicked() {
        saveClicked.postValue(Unit)
    }
}
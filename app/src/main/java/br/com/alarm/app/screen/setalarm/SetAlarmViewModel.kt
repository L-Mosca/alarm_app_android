package br.com.alarm.app.screen.setalarm

import androidx.lifecycle.MutableLiveData
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseViewModel
import br.com.alarm.app.domain.alarm.Day
import br.com.alarm.app.util.removeBrackets
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetAlarmViewModel @Inject constructor() : BaseViewModel() {

    val allDaysSelected = MutableLiveData<Int>()
    val noneDaySelected = MutableLiveData<Unit>()
    val selectedDays = MutableLiveData<String>()
    val saveSuccess = MutableLiveData<Unit>()

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

}
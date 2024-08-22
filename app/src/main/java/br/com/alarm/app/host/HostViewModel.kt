package br.com.alarm.app.host

import android.content.Intent
import androidx.lifecycle.ViewModel
import br.com.alarm.app.base.SingleLiveData
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.service.media_player.MediaPlayerContract
import br.com.alarm.app.domain.service.notification.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(private val mediaPlayer: MediaPlayerContract) :
    ViewModel() {

    val cancelAlarm = SingleLiveData<AlarmItem>()
    val scheduleAlarm = SingleLiveData<AlarmItem>()

    fun scheduleAlarm(alarm: AlarmItem) {
        scheduleAlarm.postValue(alarm)
    }

    fun cancelAlarm(alarm: AlarmItem) {
        cancelAlarm.postValue(alarm)
    }

    private fun stopPlayer() {
        mediaPlayer.stop()
    }

    fun handleIntentData(intent: Intent?) {
        intent?.let {
            if (it.action == NotificationService.NOTIFICATION_ACTION_STOP) {
                stopPlayer()
            }
        }
    }

}
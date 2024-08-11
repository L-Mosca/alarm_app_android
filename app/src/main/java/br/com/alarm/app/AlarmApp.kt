package br.com.alarm.app

import android.app.Application
import br.com.alarm.app.domain.service.NotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlarmApp : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationService.createNotificationChannel(this)
    }
}
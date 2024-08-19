package br.com.alarm.app.host

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.alarm.app.R
import br.com.alarm.app.databinding.ActivityHostBinding
import br.com.alarm.app.domain.service.notification.NotificationService
import br.com.alarm.app.domain.service.notification.NotificationService.Companion.NOTIFICATION_DEFAULT_ID
import br.com.alarm.app.domain.service.notification.NotificationService.Companion.NOTIFICATION_EXTRA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding
    private val viewModel: HostViewModel by viewModels()
    private lateinit var notificationService: NotificationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_host)
        binding = ActivityHostBinding.bind(findViewById(R.id.navHostContainer))

        notificationService = NotificationService(this)

        initObservers()
        handleIntentData(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntentData(intent)
    }

    private fun initObservers() {
        viewModel.createNotification.observe(this) {
            notificationService.showNotification(it)
        }

        viewModel.scheduleAlarm.observe(this) { notificationService.scheduleAlarm(it) }
    }

    private fun handleIntentData(intent: Intent?) {
        viewModel.handleIntentData(intent)
    }
}
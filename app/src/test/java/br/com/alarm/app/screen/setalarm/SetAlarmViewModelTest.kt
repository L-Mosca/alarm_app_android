package br.com.alarm.app.screen.setalarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.alarm.app.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SetAlarmViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun updateAlarmStatus_shouldUpdateAlarmItem_whenEnabled() {
        // TODO criar mocks dos repositories da maneira correta
        /*val viewModel = SetAlarmViewModel(ApplicationProvider.getApplicationContext())

        viewModel.updateAlarmStatus(false)

        val value = viewModel.alarmItem.getOrAwaitValue()
        assertNotNull(value)
        assertFalse(value.isEnable)*/
    }
}
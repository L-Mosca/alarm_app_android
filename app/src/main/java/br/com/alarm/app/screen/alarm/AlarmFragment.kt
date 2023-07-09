package br.com.alarm.app.screen.alarm

import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentAlarmBinding
import br.com.alarm.app.domain.alarm.AlarmItem
import br.com.alarm.app.domain.alarm.WeekDay
import br.com.alarm.app.screen.alarm.adapter.AlarmAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAlarmBinding =
        FragmentAlarmBinding::inflate
    override val viewModel: AlarmViewModel by viewModels()

    val adapter = AlarmAdapter()


    override fun initViews() {
        val alarmList: List<AlarmItem> = listOf(
            AlarmItem(
                id = 1,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 2,
                date = System.currentTimeMillis(),
                isEnable = false,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
                weekDays = listOf(
                    WeekDay(1, "S", true),
                    WeekDay(2, "M", true),
                    WeekDay(3, "T", true),
                    WeekDay(4, "W", false),
                    WeekDay(5, "T", false),
                    WeekDay(6, "F", true),
                    WeekDay(7, "S", true)
                )
            ),
        )

        adapter.dataList = alarmList

        adapter.onSwitchSelected = {

        }

        adapter.onOptionsSelected = {

        }

        binding.rvAlarms.adapter = adapter

    }

    override fun initObservers() {
    }
}
package br.com.alarm.app.screen.alarm

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentAlarmBinding
import br.com.alarm.app.domain.alarm.AlarmItem
import br.com.alarm.app.screen.alarm.adapter.AlarmAdapter
import br.com.alarm.app.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAlarmBinding =
        FragmentAlarmBinding::inflate
    override val viewModel: AlarmViewModel by viewModels()

    private val adapter = AlarmAdapter()

    override fun initViews() {

        binding.fabNewAlarm.setOnClickListener {
            val direction = AlarmFragmentDirections.actionAlarmFragmentToSetAlarmFragment()
            navigate(direction)
        }

        val alarmList: List<AlarmItem> = listOf(
            AlarmItem(
                id = 1,
                date = System.currentTimeMillis(),
                isEnable = true,
            ),
            AlarmItem(
                id = 2,
                date = System.currentTimeMillis(),
                isEnable = false,
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
            ),
            AlarmItem(
                id = 3,
                date = System.currentTimeMillis(),
                isEnable = true,
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
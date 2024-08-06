package br.com.alarm.app.screen.alarm

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.alarm.app.R
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
        binding.fabNewAlarm.setOnClickListener { goToAlarmScreen() }
        setupAdapter()
    }

    override fun initObservers() {
        viewModel.deleteAlarm.observe(viewLifecycleOwner) { position ->
            val newList = adapter.dataList.toMutableList()
            newList.removeAt(1)
            adapter.dataList = newList
            adapter.notifyItemRemoved(position)
        }

        viewModel.goToEditScreen.observe(viewLifecycleOwner) { alarm ->
            goToAlarmScreen(alarm)
        }
    }

    private fun setupAdapter() {
        val alarmList: List<AlarmItem> = listOf(
            AlarmItem(id = 1, date = System.currentTimeMillis(), isEnable = true),
            AlarmItem(id = 2, date = System.currentTimeMillis(), isEnable = false),
        )

        adapter.dataList = alarmList

        adapter.onSwitchSelected = { }

        adapter.onOptionsSelected = { view, position, alarm ->
            showPopMenu(R.menu.alarm_pop_menu, view) {
                viewModel.handlePopMenuClick(it, position, alarm)
            }
        }

        adapter.onItemSelected = { goToAlarmScreen(it) }

        binding.rvAlarms.adapter = adapter
    }

    private fun goToAlarmScreen(alarmItem: AlarmItem? = null) {
        val direction = AlarmFragmentDirections.actionAlarmFragmentToSetAlarmFragment(alarmItem)
        navigate(direction)
    }
}
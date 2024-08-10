package br.com.alarm.app.screen.alarm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentAlarmBinding
import br.com.alarm.app.domain.models.alarm.AlarmItem
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.alarmUpdated.observe(viewLifecycleOwner) { adapter.notifyItemChanged(it.second) }

        viewModel.alarmList.observe(viewLifecycleOwner) { setupAdapter(it) }

        viewModel.goToEditScreen.observe(viewLifecycleOwner) { goToAlarmScreen(it) }

        viewModel.deleteAlarm.observe(viewLifecycleOwner) {
            val list = adapter.dataList.toMutableList()
            list.remove(it.second)
            adapter.dataList = list
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupAdapter(list: List<AlarmItem>) {
        adapter.dataList = list

        adapter.onSwitchSelected = { alarm, position -> viewModel.changeAlarm(alarm, position) }

        adapter.onOptionsSelected = { view, position, alarm ->
            showPopMenu(R.menu.alarm_pop_menu, view) {
                viewModel.handlePopMenuClick(it, position, alarm)
            }
        }

        adapter.onItemSelected = { goToAlarmScreen(it) }

        binding.rvAlarms.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun goToAlarmScreen(alarmItem: AlarmItem? = null) {
        val direction =
            AlarmFragmentDirections.actionAlarmFragmentToSetAlarmFragment(alarmItem?.id ?: -100)
        navigate(direction)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAlarms()
    }
}
package br.com.alarm.app.screen.setalarm.weekdays

import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentWeekDaysBinding
import br.com.alarm.app.domain.models.alarm.Day
import br.com.alarm.app.domain.models.alarm.WeekDays
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class WeekDaysFragment : BaseFragment<FragmentWeekDaysBinding>() {

    companion object {
        private const val WEEK_DAYS_ARGUMENT = "WeekDaysFragment.WEEK_DAYS_ARGUMENT"

        fun newInstance(daysList: WeekDays) = WeekDaysFragment().apply {
            arguments = bundleOf(WEEK_DAYS_ARGUMENT to daysList)
        }
    }

    interface Listener {
        fun onClose()
        fun onSaveClicked(daysList: List<Day>)
    }

    var listener: Listener? = null

    override val bindingInflater: (LayoutInflater) -> FragmentWeekDaysBinding =
        FragmentWeekDaysBinding::inflate
    override val viewModel: WeekDaysViewModel by viewModels()
    override val screenName = "Week Days"

    private val adapter = WeekDaysAdapter()

    override fun initViews() {
        binding.ivClose.setOnClickListener { listener?.onClose() }
        binding.btSave.setOnClickListener { viewModel.onSaveClicked() }

        setupAdapter()
    }

    override fun initObservers() {
        viewModel.saveClicked.observe(viewLifecycleOwner) {
            listener?.onSaveClicked(adapter.dataList)
        }
    }

    private fun setupAdapter() {
        arguments?.getParcelable<WeekDays>(WEEK_DAYS_ARGUMENT)?.let {
            adapter.dataList = it.days
            binding.rvWeekDays.adapter = adapter
        }
        adapter.onItemClicked = { }
    }
}
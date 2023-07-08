package br.com.alarm.app.screen.alarm

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentAlarmBinding =
        FragmentAlarmBinding::inflate
    override val viewModel: AlarmViewModel by viewModels()

    override fun initViews() {

    }

    override fun initObservers() {
    }
}
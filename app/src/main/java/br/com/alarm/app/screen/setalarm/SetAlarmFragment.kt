package br.com.alarm.app.screen.setalarm

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentSetAlarmBinding
import br.com.alarm.app.domain.alarm.AlarmItem
import br.com.alarm.app.domain.alarm.Day
import br.com.alarm.app.domain.alarm.WeekDays
import br.com.alarm.app.screen.setalarm.weekdays.WeekDaysFragment
import br.com.alarm.app.util.executeDelayed
import br.com.alarm.app.util.getDifferenceTime
import br.com.alarm.app.util.hideKeyboard
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class SetAlarmFragment : BaseFragment<FragmentSetAlarmBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSetAlarmBinding =
        FragmentSetAlarmBinding::inflate
    override val viewModel: SetAlarmViewModel by viewModels()

    private val navArgs: SetAlarmFragmentArgs by navArgs()
    private var dayData: WeekDays? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun initViews() {
        viewModel.setInitialData(navArgs.alarmItem)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.handleRingtoneSelected(result)
            }

        dayData = WeekDays.buildWeekDaysList(requireContext())

        binding.ivBackArrow.setOnClickListener { findNavController().popBackStack() }
        binding.tvDone.setOnClickListener { viewModel.saveClicked() }

        binding.includeAlarmSound.clSelectSound.setOnClickListener { viewModel.selectRingtone() }
        setupHour()
        setupWeekDays()
        alarmSettingsCallbacks()
        setTimeText(binding.tpHour.hour, binding.tpHour.minute)
    }

    private fun setupHour() {
        binding.tpHour.setOnTimeChangedListener { _, hour, minute -> setTimeText(hour, minute) }
        binding.tpHour.apply { hour = 6; minute = 0 }
    }

    @SuppressLint("SetTextI18n")
    override fun initObservers() {
        viewModel.alarmItem.observe(viewLifecycleOwner) { alarm ->
            updateAlarmUi(alarm)
        }

        viewModel.ringtoneTitle.observe(viewLifecycleOwner) { title ->
            binding.includeAlarmSound.tvSoundName.text = title
        }

        viewModel.fetchRingtone.observe(viewLifecycleOwner) { intent ->
            resultLauncher.launch(intent)
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            val differenceTime =
                Pair(binding.tpHour.hour, binding.tpHour.minute).getDifferenceTime()

            Toast.makeText(
                requireContext(),
                getString(R.string.alarm_save, differenceTime.first, differenceTime.second),
                Toast.LENGTH_SHORT
            ).show()

            val triggerTime = Calendar.getInstance()
            triggerTime.add(Calendar.SECOND, 10)

            findNavController().popBackStack()
        }

        viewModel.noneDaySelected.observe(viewLifecycleOwner) {
            binding.includeWeekDays.tvWeekDaysSelected.apply {
                text = ""
                isVisible = false
            }
        }

        viewModel.selectedDays.observe(viewLifecycleOwner) { selectedDays ->
            binding.includeWeekDays.tvWeekDaysSelected.apply {
                text = selectedDays
                isVisible = true
            }
        }

        viewModel.allDaysSelected.observe(viewLifecycleOwner) { stringRes ->
            binding.includeWeekDays.tvWeekDaysSelected.apply {
                text = getString(stringRes)
                isVisible = true
            }
        }
    }

    private fun updateAlarmUi(alarmItem: AlarmItem) {}

    private fun alarmSettingsCallbacks() {
        binding.includeEnableAlarm.apply {
            vSetAlarm.setOnClickListener {
                swEnableAlarm.isChecked = !swEnableAlarm.isChecked
                changeSwitchAppearance(swEnableAlarm.isChecked, swEnableAlarm)
            }
            swEnableAlarm.setOnCheckedChangeListener { _, isChecked ->
                changeSwitchAppearance(isChecked, swEnableAlarm)
            }
        }
    }

    private fun setupWeekDays() {
        binding.includeWeekDays.vWeekDays.setOnClickListener {
            val fragment = WeekDaysFragment.newInstance(dayData!!)

            fragment.listener = object : WeekDaysFragment.Listener {
                override fun onClose() {
                    runCircularRevealAnimation(false)
                    executeDelayed(500L) {
                        childFragmentManager.commit {
                            remove(fragment)
                        }
                    }
                }

                override fun onSaveClicked(daysList: List<Day>) {
                    dayData?.days = daysList
                    viewModel.setSelectedDays(dayData?.days!!)
                    runCircularRevealAnimation(false)
                }
            }

            childFragmentManager.commit {
                replace(binding.flWeekDays.id, fragment)
                runOnCommit {
                    runCircularRevealAnimation(true)
                }
            }
        }
    }

    private fun runCircularRevealAnimation(isOpening: Boolean) {
        hideKeyboard()
        val anim = if (isOpening) binding.flWeekDays.animate().alpha(1f).setDuration(300L)
        else binding.flWeekDays.animate().alpha(0f).setDuration(300L)

        if (isOpening) {
            binding.flWeekDays.visibility = View.VISIBLE
            binding.flWeekDays.alpha = 0f
        } else anim.withEndAction { binding.flWeekDays.visibility = View.INVISIBLE }

        anim.start()
    }

    private fun changeSwitchAppearance(isEnable: Boolean, switch: SwitchMaterial) {
        switch.trackTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (isEnable) R.color.pink_500 else R.color.blue_600
        )
    }

    @SuppressLint("DefaultLocale")
    private fun setTimeText(hour: Int, minute: Int) {
        val formattedHour = String.format("%02d:%02d", hour, minute)
        binding.tvWakeHour.text = formattedHour
    }
}
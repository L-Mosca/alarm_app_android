package br.com.alarm.app.screen.setalarm

import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentSetAlarmBinding
import br.com.alarm.app.domain.alarm.Day
import br.com.alarm.app.domain.alarm.WeekDays
import br.com.alarm.app.screen.setalarm.weekdays.WeekDaysFragment
import br.com.alarm.app.util.executeDelayed
import br.com.alarm.app.util.getDifferenceTime
import br.com.alarm.app.util.hideKeyboard
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetAlarmFragment : BaseFragment<FragmentSetAlarmBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSetAlarmBinding =
        FragmentSetAlarmBinding::inflate
    override val viewModel: SetAlarmViewModel by viewModels()

    private var dayData: WeekDays? = null

    override fun initViews() {
        dayData = WeekDays(
            days = listOf(
                Day(id = 0, dayName = getString(R.string.sunday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.monday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.tuesday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.wednesday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.thursday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.friday), isEnable = false),
                Day(id = 0, dayName = getString(R.string.saturday), isEnable = false),
            )
        )

        binding.ivBackArrow.setOnClickListener { findNavController().popBackStack() }
        binding.tvDone.setOnClickListener { viewModel.saveClicked() }
        binding.includeAlarmSound.tvSoundProgress.text =
            binding.includeAlarmSound.sbVolume.progress.toString()
        binding.tpHour.setOnTimeChangedListener { _, hourOfDay, minute ->
            setTimeText(hourOfDay, minute)
        }
        alarmSettingsCLick()
        setInitialHour()
        setTimeText(binding.tpHour.hour, binding.tpHour.minute)
    }

    override fun initObservers() {
        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            val differenceTime =
                Pair(binding.tpHour.hour, binding.tpHour.minute).getDifferenceTime()

            Toast.makeText(
                requireContext(),
                getString(R.string.alarm_save, differenceTime.first, differenceTime.second),
                Toast.LENGTH_SHORT
            ).show()
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

    private fun alarmSettingsCLick() {
        binding.apply {
            includeEnableAlarm.apply {
                vSetAlarm.setOnClickListener {
                    swEnableAlarm.isChecked = !swEnableAlarm.isChecked
                    changeSwitchAppearance(swEnableAlarm.isChecked, swEnableAlarm)
                }
                swEnableAlarm.setOnCheckedChangeListener { _, isChecked ->
                    changeSwitchAppearance(isChecked, swEnableAlarm)
                }
            }

            includeAlarmSound.apply {
                vSelectSound.setOnClickListener { }
                sbVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        tvSoundProgress.text = String.format("%02d", progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }

            includeSnooze.apply {
                vSnooze.setOnClickListener {
                    swEnableSnooze.isChecked = !swEnableSnooze.isChecked
                    changeSwitchAppearance(swEnableSnooze.isChecked, swEnableSnooze)
                }
                swEnableSnooze.setOnCheckedChangeListener { _, isChecked ->
                    changeSwitchAppearance(isChecked, swEnableSnooze)
                }
            }

            includeWeekDays.vWeekDays.setOnClickListener {
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
    }

    private fun runCircularRevealAnimation(isOpening: Boolean) {
        hideKeyboard()
        //Create the animation
        val anim = if (isOpening)
            binding.flWeekDays.animate()
                .alpha(1f)
                .setDuration(300L)
        else
            binding.flWeekDays.animate()
                .alpha(0f)
                .setDuration(300L)
        if (isOpening) {
            // make the view visible and start the animation
            binding.flWeekDays.visibility = View.VISIBLE
            binding.flWeekDays.alpha = 0f
        } else {
            anim.withEndAction {
                binding.flWeekDays.visibility = View.INVISIBLE
            }
        }
        //Start animation
        anim.start()
    }

    private fun changeSwitchAppearance(isEnable: Boolean, switch: SwitchMaterial) {
        switch.trackTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (isEnable) R.color.pink_500 else R.color.blue_600
        )
    }

    private fun setInitialHour() {
        binding.tpHour.apply { hour = 6; minute = 0 }
    }

    private fun setTimeText(hour: Int, minute: Int) {
        val formattedHour = String.format("%02d:%02d", hour, minute)
        binding.tvWakeHour.text = formattedHour
    }
}
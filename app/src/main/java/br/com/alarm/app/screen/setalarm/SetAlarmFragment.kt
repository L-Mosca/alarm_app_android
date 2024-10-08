package br.com.alarm.app.screen.setalarm

import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseFragment
import br.com.alarm.app.databinding.FragmentSetAlarmBinding
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.WeekDays
import br.com.alarm.app.host.HostViewModel
import br.com.alarm.app.screen.setalarm.confirm_alarm_dialog.ConfirmAlarmDialog
import br.com.alarm.app.util.extractHoursAndMinutesFromTimestamp
import br.com.alarm.app.util.getDifferenceTime
import br.com.alarm.app.util.getHourIn24Format
import br.com.alarm.app.util.getRingToneTitle
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SetAlarmFragment : BaseFragment<FragmentSetAlarmBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentSetAlarmBinding =
        FragmentSetAlarmBinding::inflate
    override val viewModel: SetAlarmViewModel by viewModels()
    override val screenName = "Create Alarm"

    private val mainViewModel: HostViewModel by activityViewModels()
    private val navArgs: SetAlarmFragmentArgs by navArgs()
    private var dayData: WeekDays? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun initViews() {
        setBackNavigation {
            viewModel.handleBackPressed(::defaultBackPressed, ::showConfirmationDialog)
        }
        dayData = WeekDays.buildWeekDaysList()
        viewModel.setInitialData(navArgs.alarmItem)
        setupResultLauncher()
        uiInitialSetup()
    }

    /**
     * UI callbacks setup.
     */
    private fun uiInitialSetup() {
        setupAlarmTitle()
        setupAlarmHour()
        setupAlarmSettings()
        setupBottomButtons()

        binding.includeSetAlarmHour.apply {
            val hour = tpHour.hour
            val minute = tpHour.minute
            binding.includeSetAlarmHour.tvWakeHour.text = getHourIn24Format(hour, minute)
        }
    }

    /**
     * It's called when user back from ringtone selector Intent.
     * Handle selected ringtone and apply result in UI (viewModel.handleRingtoneSelected)
     */
    private fun setupResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.handleRingtoneSelected(result)
            }
    }

    /**
     * Setup alarm "AppBar"
     * includeSetAlarmTitle.
     *  -> Screen Title {tvSetAlarm}
     *  -> Return button {ivBackArrow}
     */
    private fun setupAlarmTitle() {
        binding.includeSetAlarmTitle.apply {
            vBackArrow.setOnClickListener {
                viewModel.handleBackPressed(
                    ::defaultBackPressed,
                    ::showConfirmationDialog
                )
            }
            vDeleteAlarm.setOnClickListener { viewModel.deleteAlarm() }
        }
    }

    /**
     * Setup alarm time picker and hour card
     * includeSetAlarmHour.
     *  -> Time picker {tpHour}
     *  -> includeWarning.
     *      -> Warning message
     *  -> Time selected {tvWakeHour}
     */
    private fun setupAlarmHour() {
        binding.includeSetAlarmHour.tpHour.apply {
            setIs24HourView(DateFormat.is24HourFormat(requireContext()))

            setOnTimeChangedListener { _, hour, minute ->
                viewModel.updateAlarmTime(hour, minute)
            }
        }
    }

    /**
     * Setup alarm settings:
     *
     * includeSetAlarmSettings.
     *  -> Enabled {swEnableAlarm} -> Show/Switch if alarm was enabled
     *  -> Sound {clSelectSound} -> Show/Select alarm ringtone sound
     *  -> WeekDays {vWeekDays} -> Show/Select alarm week days
     *  -> WeekDaysFragment {flWeekDays} -> Inflate week days popup layout
     */
    private fun setupAlarmSettings() {
        binding.includeSetAlarmSettings.apply {

            // Setup Enable/Disable switch
            includeEnableAlarm.apply {
                vSetAlarm.setOnClickListener {
                    viewModel.updateAlarmStatus(!swEnableAlarm.isChecked)
                }
            }

            // Setup Sound
            includeAlarmSound.clSelectSound.setOnClickListener { viewModel.selectRingtone() }

            // Setup Week days
            /*includeWeekDays.vWeekDays.setOnClickListener {
                val fragment = WeekDaysFragment.newInstance(dayData!!)

                fragment.listener = object : WeekDaysFragment.Listener {
                    override fun onClose() {
                        runCircularRevealAnimation(false)
                        executeDelayed(500L) { childFragmentManager.commit { remove(fragment) } }
                    }

                    override fun onSaveClicked(daysList: List<Day>) {
                        dayData?.days = daysList
                        viewModel.setSelectedDays(dayData?.days!!)
                        includeWeekDays.tvWeekDaysSelected.text =
                            dayData.getWeekDays(requireContext())
                        runCircularRevealAnimation(false)
                    }
                }

                childFragmentManager.commit {
                    replace(binding.flWeekDays.id, fragment)
                    runOnCommit { runCircularRevealAnimation(true) }
                }
            }*/
        }
    }

    /**
     * Setup bottom buttons
     * - cancel button {btCancelAlarm} -> Cancel changes and back to previous screen
     * - save button {btSaveAlarm} -> Save alarm and back to previous screen
     */
    private fun setupBottomButtons() {
        binding.includeSetAlarmButtons.apply {
            btSaveAlarm.setOnClickListener { viewModel.saveClicked() }
        }
    }


    override fun initObservers() {
        viewModel.deleteSuccess.observe(viewLifecycleOwner) { findNavController().popBackStack() }

        viewModel.alarmItem.observe(viewLifecycleOwner) { updateAlarmUi(it) }

        viewModel.fetchRingtone.observe(viewLifecycleOwner) { resultLauncher.launch(it) }

        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            binding.includeSetAlarmHour.tpHour.apply {
                val differenceTime = Pair(hour, minute).getDifferenceTime()
                val toastMessage =
                    getString(R.string.alarm_save, differenceTime.first, differenceTime.second)

                showShortToast(toastMessage)
                val triggerTime = Calendar.getInstance()
                triggerTime.add(Calendar.SECOND, 10)
                mainViewModel.scheduleAlarm(it)
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Update all screen UI when user change any data
     */
    private fun updateAlarmUi(alarmItem: AlarmItem) {
        // Update toolbar UI
        binding.includeSetAlarmTitle.apply {
            ivDeleteAlarm.isVisible = alarmItem.id != null
            vDeleteAlarm.isVisible = alarmItem.id != null
        }

        // Update alarm hour in time picker and card hour
        binding.includeSetAlarmHour.apply {
            val (hour, minute) = extractHoursAndMinutesFromTimestamp(alarmItem.date)
            tpHour.hour = hour
            tpHour.minute = minute
            tvWakeHour.text = getHourIn24Format(hour, minute)
        }

        binding.includeSetAlarmSettings.apply {

            // Update switch checked status
            includeEnableAlarm.swEnableAlarm.isChecked = alarmItem.isEnable
            changeSwitchAppearance(alarmItem.isEnable, includeEnableAlarm.swEnableAlarm)

            // Update ringtone name
            includeAlarmSound.tvSoundName.text =
                alarmItem.ringtone.getRingToneTitle(requireContext())

            // Update week days
            dayData = alarmItem.weekDays

            //includeWeekDays.tvWeekDaysSelected.text = dayData.getWeekDays(requireContext())
        }
    }

    /*private fun runCircularRevealAnimation(isOpening: Boolean) {
        hideKeyboard()
        val anim = if (isOpening) binding.flWeekDays.animate().alpha(1f).setDuration(300L)
        else binding.flWeekDays.animate().alpha(0f).setDuration(300L)

        if (isOpening) {
            binding.flWeekDays.visibility = View.VISIBLE
            binding.flWeekDays.alpha = 0f
        } else anim.withEndAction { binding.flWeekDays.visibility = View.INVISIBLE }

        anim.start()
    }*/

    private fun changeSwitchAppearance(isEnable: Boolean, switch: SwitchMaterial) {
        switch.trackTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (isEnable) R.color.pink_500 else R.color.blue_600
        )
    }

    private fun showConfirmationDialog() {
        val fragment = ConfirmAlarmDialog().apply {
            onSaveClicked = {
                dismiss()
                this@SetAlarmFragment.viewModel.saveClicked()
            }
            onCloseClicked = {
                dismiss()
                findNavController().popBackStack()
            }
        }
        fragment.show(childFragmentManager, "CONFIRM_DIALOG")
    }

    private fun defaultBackPressed() {
        findNavController().popBackStack()
    }
}
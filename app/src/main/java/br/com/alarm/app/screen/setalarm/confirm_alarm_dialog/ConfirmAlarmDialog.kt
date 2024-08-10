package br.com.alarm.app.screen.setalarm.confirm_alarm_dialog

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.alarm.app.base.BaseDialogFragment
import br.com.alarm.app.databinding.DialogConfirmAlarmEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmAlarmDialog : BaseDialogFragment<DialogConfirmAlarmEditBinding>() {

    override val bindingInflater: (LayoutInflater) -> DialogConfirmAlarmEditBinding =
        DialogConfirmAlarmEditBinding::inflate

    override val viewModel: ConfirmAlarmDialogViewModel by viewModels()

    var onCloseClicked: ((Unit) -> Unit)? = null
    var onSaveClicked: ((Unit) -> Unit)? = null

    override fun initViews() {
        binding.apply {
            setBackNavigation { dismiss() }
            rlConfirmAlarm.setOnClickListener { dismiss() }

            btConfirmClose.setOnClickListener { onCloseClicked?.invoke(Unit) }
            btConfirmSave.setOnClickListener { onSaveClicked?.invoke(Unit) }
        }
    }

    override fun initObservers() { }

    override fun getBundleArguments() { }
}
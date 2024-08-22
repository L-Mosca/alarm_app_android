package br.com.alarm.app.screen.alarm.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseAdapter
import br.com.alarm.app.base.ViewHolder
import br.com.alarm.app.databinding.AdapterAlarmItemBinding
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.util.get12HourFormatTag
import br.com.alarm.app.util.getDate
import br.com.alarm.app.util.getRingToneTitle

class AlarmAdapter : BaseAdapter<AdapterAlarmItemBinding, AlarmItem>() {
    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterAlarmItemBinding
        get() = { layoutInflater, viewGroup ->
            AdapterAlarmItemBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onSwitchSelected: ((AlarmItem, Int) -> Unit)? = null
    var onOptionsSelected: ((View, Int, AlarmItem) -> Unit)? = null
    var onItemSelected: ((AlarmItem) -> Unit)? = null
    private var is24HourFormat = true
    private var isFirstTime = true

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterAlarmItemBinding>,
        data: AlarmItem,
        position: Int
    ) {
        if (isFirstTime) {
            isFirstTime = false
            is24HourFormat = DateFormat.is24HourFormat(holder.binding.root.context)
        }

        holder.binding.apply {
            root.setOnClickListener { onItemSelected?.invoke(data) }
            vOptions.setOnClickListener { onOptionsSelected?.invoke(it, position, data) }

            vSwitch.setOnClickListener {
                data.isEnable = !data.isEnable
                onSwitchSelected?.invoke(data, position)
            }

            tvHour.text = data.date.getDate(is24HourFormat)
            tvHourFormat.isVisible = !is24HourFormat
            if (!is24HourFormat) tvHourFormat.text = data.date.get12HourFormatTag()

            swAlarm.isChecked = data.isEnable

            //tvWeekDays.text = data.weekDays.getWeekDays(root.context)
            tvWeekDays.text = data.ringtone.getRingToneTitle(root.context)

            vShadow.isVisible = !data.isEnable
            val color = if (data.isEnable) R.color.pink_500 else R.color.blue_600
            swAlarm.trackTintList = ContextCompat.getColorStateList(root.context, color)
        }
    }
}
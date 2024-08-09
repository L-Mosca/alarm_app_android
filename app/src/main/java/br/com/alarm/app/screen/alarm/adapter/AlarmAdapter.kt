package br.com.alarm.app.screen.alarm.adapter

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseListAdapter
import br.com.alarm.app.base.ViewHolder
import br.com.alarm.app.databinding.AdapterAlarmItemBinding
import br.com.alarm.app.domain.models.alarm.AlarmItem
import br.com.alarm.app.domain.models.alarm.getWeekDays
import br.com.alarm.app.util.get12HourFormatTag
import br.com.alarm.app.util.getDate

class AlarmAdapter : BaseListAdapter<AdapterAlarmItemBinding, AlarmItem>(DiffUtilCallback) {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> AdapterAlarmItemBinding
        get() = { layoutInflater, viewGroup, _ ->
            AdapterAlarmItemBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    object DiffUtilCallback : DiffUtil.ItemCallback<AlarmItem>() {
        override fun areItemsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
            return oldItem == newItem
        }

    }

    var onSwitchSelected: ((Unit) -> Unit)? = null
    var onOptionsSelected: ((View, Int, AlarmItem) -> Unit)? = null
    var onItemSelected: ((AlarmItem) -> Unit)? = null
    private var is24HourFormat = true
    private var isFirstTime = true

    @SuppressLint("SetTextI18n", "DefaultLocale")
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

            tvHour.text = data.date.getDate(is24HourFormat)
            tvHourFormat.isVisible = !is24HourFormat
            if (!is24HourFormat) tvHourFormat.text = data.date.get12HourFormatTag()

            swAlarm.isChecked = data.isEnable

            tvWeekDays.text = data.weekDays.getWeekDays(root.context)

            vSwitch.setOnClickListener {
                data.isEnable = !data.isEnable
                swAlarm.isChecked = data.isEnable
                onSwitchSelected?.invoke(Unit)
                vShadow.isVisible = !data.isEnable
                val color = if (data.isEnable) R.color.pink_500 else R.color.blue_600
                swAlarm.trackTintList = ContextCompat.getColorStateList(root.context, color)
            }

            vShadow.isVisible = !data.isEnable
            val color = if (data.isEnable) R.color.pink_500 else R.color.blue_600
            swAlarm.trackTintList = ContextCompat.getColorStateList(root.context, color)

        }
    }
}
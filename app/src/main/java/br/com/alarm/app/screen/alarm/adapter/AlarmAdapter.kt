package br.com.alarm.app.screen.alarm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.alarm.app.R
import br.com.alarm.app.base.BaseAdapter
import br.com.alarm.app.base.ViewHolder
import br.com.alarm.app.databinding.AdapterAlarmItemBinding
import br.com.alarm.app.domain.alarm.AlarmItem
import java.util.Calendar
import java.util.Date

class AlarmAdapter : BaseAdapter<AdapterAlarmItemBinding, AlarmItem>() {
    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterAlarmItemBinding
        get() = { layoutInflater, viewGroup ->
            AdapterAlarmItemBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onSwitchSelected: ((Unit) -> Unit)? = null
    var onOptionsSelected: ((Unit) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder<AdapterAlarmItemBinding>,
        data: AlarmItem,
        position: Int
    ) {
        holder.binding.apply {
            val date = Date(data.date)

            val calendar = Calendar.getInstance()
            calendar.time = date


            val hour = calendar.get(Calendar.HOUR_OF_DAY) + 1
            val minute = calendar.get(Calendar.MINUTE)

            tvHour.text = String.format("%02d:%02d", hour, minute)


            ivOptions.setOnClickListener {
                onOptionsSelected?.invoke(Unit)
            }

            swAlarm.setOnCheckedChangeListener { _, isChecked ->
                data.isEnable = isChecked
                onSwitchSelected?.invoke(Unit)

                if (data.isEnable) {
                    vShadow.isVisible = false
                    swAlarm.trackTintList =
                        ContextCompat.getColorStateList(root.context, R.color.pink_500)
                } else {
                    vShadow.isVisible = true
                    swAlarm.trackTintList =
                        ContextCompat.getColorStateList(root.context, R.color.blue_600)
                }
            }

            if (data.isEnable) {
                vShadow.isVisible = false
                swAlarm.trackTintList =
                    ContextCompat.getColorStateList(root.context, R.color.pink_500)
            } else {
                vShadow.isVisible = true
                swAlarm.trackTintList =
                    ContextCompat.getColorStateList(root.context, R.color.blue_600)
            }
        }
    }
}
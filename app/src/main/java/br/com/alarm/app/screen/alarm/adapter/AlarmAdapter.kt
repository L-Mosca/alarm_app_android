package br.com.alarm.app.screen.alarm.adapter

import android.annotation.SuppressLint
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
    var onOptionsSelected: ((View, Int, AlarmItem) -> Unit)? = null
    var onItemSelected: ((AlarmItem) -> Unit)? = null

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(
        holder: ViewHolder<AdapterAlarmItemBinding>,
        data: AlarmItem,
        position: Int
    ) {
        holder.binding.apply {
            root.setOnClickListener { onItemSelected?.invoke(data) }
            vOptions.setOnClickListener { onOptionsSelected?.invoke(it, position, data) }

            val date = Date(data.date!!)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hour = calendar.get(Calendar.HOUR_OF_DAY) + 1
            val minute = calendar.get(Calendar.MINUTE)

            tvHour.text = String.format("%02d:%02d", hour, minute)

            swAlarm.setOnCheckedChangeListener { _, isChecked ->
                data.isEnable = isChecked
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
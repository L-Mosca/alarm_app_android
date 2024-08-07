package br.com.alarm.app.screen.setalarm.weekdays

import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.alarm.app.base.BaseAdapter
import br.com.alarm.app.base.ViewHolder
import br.com.alarm.app.databinding.AdapterWeekDaysBinding
import br.com.alarm.app.domain.models.alarm.Day

class WeekDaysAdapter : BaseAdapter<AdapterWeekDaysBinding, Day>() {
    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterWeekDaysBinding
        get() = { layoutInflater, viewGroup ->
            AdapterWeekDaysBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onItemClicked: ((Unit) -> Unit)? = null

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterWeekDaysBinding>,
        data: Day,
        position: Int
    ) {
        holder.binding.apply {
            tvWeekDay.text = data.dayName
            cbWeekDay.isChecked = data.isEnable

            vWeekDays.setOnClickListener {
                val isChecked = !data.isEnable
                cbWeekDay.isChecked = isChecked
                data.isEnable = isChecked

                onItemClicked?.invoke(Unit)
            }
        }
    }
}
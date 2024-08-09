package br.com.alarm.app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<VB : ViewBinding, T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, ViewHolder<VB>>(diffCallback) {

    abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = bindingInflater(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val data = getItem(position)
        onBindViewHolder(holder, data, position)
    }

    abstract fun onBindViewHolder(holder: ViewHolder<VB>, data: T, position: Int)

    fun remove(position: Int) {
        if (currentList.size == 1) {
            submitList(emptyList())
            return
        }
        submitList(currentList.toMutableList().apply { remove(getItem(position)) })
    }
}
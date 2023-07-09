package br.com.alarm.app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<VB: ViewBinding, T>: RecyclerView.Adapter<ViewHolder<VB>>() {

    var dataList: List<T> = arrayListOf()

    abstract val bindingInflater: (LayoutInflater, ViewGroup) -> VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = bindingInflater(inflater, parent)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        val data = dataList[position]
        onBindViewHolder(holder, data, position)
    }

    abstract fun onBindViewHolder(holder: ViewHolder<VB>, data: T, position: Int)

    override fun getItemCount() = dataList.size

}

class ViewHolder<VB: ViewBinding>(
    val binding: VB
): RecyclerView.ViewHolder(binding.root)
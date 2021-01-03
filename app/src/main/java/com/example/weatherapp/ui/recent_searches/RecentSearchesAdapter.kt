package com.example.weatherapp.ui.recent_searches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.RecentSearchesEntry
import com.example.weatherapp.databinding.ListItemRecentSearchesBinding
import kotlinx.android.synthetic.main.list_item_recent_searches.view.*


class RecentSearchesAdapter internal constructor(
    private val callback: Callback
) : RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder>() {

    private var list = emptyList<RecentSearchesEntry>()
    private var deleteItemIdList = ArrayList<String>()

    private var isDeleteMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: ListItemRecentSearchesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_recent_searches,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            if (!isDeleteMode)
                callback.onItemClickListener(list[position])
        }
        if (isDeleteMode)
            holder.itemView.checkbox.visibility = View.VISIBLE
        else
            holder.itemView.checkbox.visibility = View.GONE

        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                deleteItemIdList.add(list[position].id)
            } else {
                deleteItemIdList.remove(list[position].id)
            }
        }
    }

    override fun getItemCount() = list.size

    fun setDeleteMode(enable: Boolean) {
        isDeleteMode = enable
        notifyDataSetChanged()
    }

    fun getDeleteItemList() = deleteItemIdList

    internal fun setRecentSearchesList(list: List<RecentSearchesEntry>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val dataBinding: ListItemRecentSearchesBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun setData(item: RecentSearchesEntry) {
            dataBinding.displayModel = item
        }
    }

    interface Callback {
        fun onItemClickListener(recentSearchesEntry: RecentSearchesEntry)
    }


}



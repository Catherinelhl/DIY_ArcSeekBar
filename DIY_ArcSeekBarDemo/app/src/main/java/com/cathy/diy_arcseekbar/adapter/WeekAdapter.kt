package com.cathy.diy_arcseekbar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cathy.diy_arcseekbar.R
import com.cathy.diy_arcseekbar.bean.WeekBean
import kotlinx.android.synthetic.main.item_week.view.*


/**
+--------------------------------------+
+ @author Catherine Liu
+--------------------------------------+
+ 2020/5/19 16:00
+--------------------------------------+
+ Des:显示一周数据的适配
+--------------------------------------+
 */

class WeekAdapter(val context: Context, var weekList: List<WeekBean>?) :
    RecyclerView.Adapter<WeekAdapter.WeekAdapterViewHolder>() {

    inner class WeekAdapterViewHolder(view: View) : ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekAdapterViewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_week, parent,false)
        return WeekAdapterViewHolder(view = view)
    }

    override fun getItemCount(): Int = weekList?.size ?: 0

    override fun onBindViewHolder(holder: WeekAdapterViewHolder, position: Int) {
        weekList?.let {
            var weekBean: WeekBean = it.get(position)
            holder.itemView.cb.text = weekBean.weekName
            holder.itemView.cb.isChecked = weekBean.isSelected
            holder.itemView.cb.setOnCheckedChangeListener { buttonView, isChecked -> println("isSelected:" + isChecked) }
        }
    }

}
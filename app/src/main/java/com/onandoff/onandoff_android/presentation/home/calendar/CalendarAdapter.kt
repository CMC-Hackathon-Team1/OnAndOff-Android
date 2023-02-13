package com.onandoff.onandoff_android.presentation.home.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.databinding.ItemCalendarBinding
import java.util.*

class CalendarAdapter(private val onMonthChangeListener: OnMonthChangeListener? = null) : RecyclerView.Adapter<CalendarAdapter.CalendarItemViewHolder>() {

    private val baseCalendar = BaseCalendar()
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var dateMap: MutableMap<String, List<String>>

    init {
        baseCalendar.initBaseCalendar {
            onMonthChangeListener?.onMonthChanged(it)
        }
        dateMap = mutableMapOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCalendarBinding.inflate(layoutInflater, parent, false)
        return CalendarItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return BaseCalendar.LOW_OF_CALENDAR * BaseCalendar.DAYS_OF_WEEK
    }

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            onMonthChangeListener?.onMonthChanged(it)
            notifyDataSetChanged()
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            onMonthChangeListener?.onMonthChanged(it)
            notifyDataSetChanged()
        }
    }

    interface OnMonthChangeListener {
        fun onMonthChanged(calendar : Calendar)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class CalendarItemViewHolder(private val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Int, map: MutableMap<String, List<String>>, position: Int) {
            binding.tvDate.text = date.toString()

            if (position < baseCalendar.preMonth
                || position >= baseCalendar.preMonth + baseCalendar.currentMonth) {
                binding.item.visibility = View.GONE
            } else {
                binding.item.visibility = View.VISIBLE
            }

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
//                    itemClickListener.onClick(itemView,pos)
                }
            }
        }
    }

    fun setItems(item: Map<String, List<String>>) {
        dateMap.clear()
        dateMap.putAll(item)
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        if (holder is CalendarItemViewHolder) {
            holder.bind(baseCalendar.data[position], dateMap, position)
        }
    }
}
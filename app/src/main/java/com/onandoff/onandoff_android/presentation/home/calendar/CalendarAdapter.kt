package com.onandoff.onandoff_android.presentation.home.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.CalendarData
import com.onandoff.onandoff_android.databinding.ItemCalendarBinding
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class CalendarAdapter(private val onMonthChangeListener: OnMonthChangeListener? = null) : RecyclerView.Adapter<CalendarAdapter.CalendarItemViewHolder>() {

    private val baseCalendar = BaseCalendar()
    private lateinit var itemClickListener: OnItemClickListener
    private var feedList: List<CalendarData>? = null


    init {
        baseCalendar.initBaseCalendar {
            onMonthChangeListener?.onMonthChanged(it)
        }
        notifyDataSetChanged()
        Log.d("CalenderAdapter", "init")
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
            removeItem()
            onMonthChangeListener?.onMonthChanged(it)
            notifyDataSetChanged()
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            removeItem()
            onMonthChangeListener?.onMonthChanged(it)
            notifyDataSetChanged()
        }
    }

    interface OnMonthChangeListener {
        fun onMonthChanged(calendar : Calendar)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, feedId: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class CalendarItemViewHolder(private val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Int, position: Int) {
            binding.tvDate.text = date.toString()

            if (position < baseCalendar.preMonth
                || position >= baseCalendar.preMonth + baseCalendar.currentMonth) {
                binding.item.visibility = View.GONE
            } else {
                binding.item.visibility = View.VISIBLE
            }

            if (!feedList.isNullOrEmpty()) {
                for (feed in feedList!!) {
                    if(feed.day.toInt() == date) {
                        if (!feed.feedImgUrl.isNullOrEmpty()) {
                            binding.sivCalendar.visibility = View.VISIBLE
                            binding.point.visibility = View.GONE
                            Glide.with(itemView)
                                .load(feed.feedImgUrl)
                                .into(binding.sivCalendar)
                        } else {
                            binding.point.visibility = View.VISIBLE
                            binding.sivCalendar.visibility = View.GONE
                        }

                        itemView.setOnClickListener {
                            itemClickListener.onClick(itemView,position,feed.feedId.toInt())
                        }
                    }
                }
            } else {
                binding.sivCalendar.visibility = View.GONE
                binding.point.visibility = View.GONE
            }
        }
    }
    fun setItems(item: List<CalendarData>) {
        feedList = item
        notifyDataSetChanged()
    }

    fun removeItem() {
        feedList = null
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        holder.bind(baseCalendar.data[position], position)
    }
}
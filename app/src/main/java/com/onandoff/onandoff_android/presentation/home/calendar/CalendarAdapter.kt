package com.onandoff.onandoff_android.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.CalendarData
import com.onandoff.onandoff_android.databinding.ItemCalendarBinding

class CalendarAdapter(private val dayClick: (day: String) -> Unit) :
    ListAdapter<CalendarData, CalendarAdapter.CalendarViewHolder>(
        CalendarDiffUtil
    ) {

    inner class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarData: CalendarData, dayClick: (day: String) -> Unit) { // databinding 수정하기
            binding.calendar = calendarData
            if (calendarData.isCurrentMonth) {
                binding.tvDay.visibility = View.VISIBLE
                if (calendarData.isExist) {
                    binding.sivCalendar.isVisible = true

                    Glide.with(binding.root.context)
                        .load(calendarData.imageUrl)
                        .into(binding.sivCalendar)

                    binding.root.setOnClickListener {
                        dayClick(calendarData.day)
                    }
                } else {
                    binding.sivCalendar.isVisible = false
                }
            } else {
                binding.tvDay.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position), dayClick)
    }

    companion object CalendarDiffUtil : DiffUtil.ItemCallback<CalendarData>() {
        override fun areItemsTheSame(oldItem: CalendarData, newItem: CalendarData): Boolean {
            return (oldItem.day == newItem.day) && (oldItem.isCurrentMonth == newItem.isCurrentMonth)
        }

        override fun areContentsTheSame(oldItem: CalendarData, newItem: CalendarData) =
            oldItem == newItem
    }
}
package com.onandoff.onandoff_android.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.Calendar
import com.onandoff.onandoff_android.databinding.ItemCalendarBinding

class CalendarAdapter(private val dayClick: (day: String) -> Unit) :
    ListAdapter<Calendar, CalendarAdapter.CalendarViewHolder>(
        CalendarDiffUtil
    ) {

    inner class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendar: Calendar, dayClick: (day: String) -> Unit) { // databinding 수정하기
            binding.calendar = calendar
            if (calendar.isCurrentMonth) {
                binding.tvDay.visibility = View.VISIBLE
                if (calendar.isExist) {
                    binding.sivCalendar.isVisible = true

                    Glide.with(binding.root.context)
                        .load(calendar.imageUrl)
                        .into(binding.sivCalendar)

                    binding.root.setOnClickListener {
                        dayClick(calendar.day)
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

    companion object CalendarDiffUtil : DiffUtil.ItemCallback<Calendar>() {
        override fun areItemsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
            return (oldItem.day == newItem.day) && (oldItem.isCurrentMonth == newItem.isCurrentMonth)
        }

        override fun areContentsTheSame(oldItem: Calendar, newItem: Calendar) =
            oldItem == newItem
    }
}
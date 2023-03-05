package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.CalendarResponse
import com.onandoff.onandoff_android.databinding.FragmentOtherUserBinding
import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingReadActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OtherUserFragment: Fragment(), CalendarAdapter.OnMonthChangeListener, CalendarAdapter.OnItemClickListener  {
    private var _binding: FragmentOtherUserBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private var profileId: Int? = null
    private var otherUserId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMonthChanged(calendar: Calendar) {
        val userId = if(profileId != null) profileId!! else 0
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        var monthFormat = month.toString()
        if (month < 10) {
            monthFormat = "0${monthFormat}"
        }

        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        binding.fgCalMonth.text = sdf.format(calendar.time)

        val calendarInterface: CalendarInterface? = RetrofitClient.getClient()?.create(
            CalendarInterface::class.java)
        val call = calendarInterface?.getCalendarList(userId, year, monthFormat)
        call?.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(
                call: Call<CalendarResponse>,
                response: Response<CalendarResponse>
            ) {
                Log.d("feedList", "onResponse: ${response.code()}")

                when(response.code()) {
                    200 -> {
                        val feedList = response.body()?.result
                        Log.d("feedList", "onResponse: ${feedList?.size}")
                        if (!feedList.isNullOrEmpty()) {
                            calendarAdapter.setItems(feedList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: calendar error ${t.message}")
            }

        })
    }

    private fun setupCalendar() {
        val baseCalendar = BaseCalendar()

        baseCalendar.initBaseCalendar {
            onMonthChanged(it)
        }

        calendarAdapter = CalendarAdapter(this)
        calendarAdapter.setItemClickListener(this)
        binding.fgCalDay.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        binding.fgCalDay.adapter = calendarAdapter

        binding.fgCalPre.setOnClickListener {
            calendarAdapter.changeToPrevMonth()
        }
        binding.fgCalNext.setOnClickListener {
            calendarAdapter.changeToNextMonth()
        }
    }

    override fun onClick(v: View, position: Int, feedId: Int) {
        intentPostReadActivity(feedId)
    }

    private fun intentPostReadActivity(feedId: Int) {
        //intent로 profileId랑 feedId를 보내야함
        val intent = Intent(requireActivity(), PostingReadActivity::class.java)
        intent.putExtra("otherUserId", otherUserId)
        intent.putExtra("profileId", profileId)
        intent.putExtra("feedId", feedId)
        startActivity(intent)
    }
}
package com.onandoff.onandoff_android.presentation.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentHomeBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
            get() = _binding!!

    private lateinit var myProfileListAdapter: MyProfileListAdapter
    private lateinit var relevantUserListAdapter: RelevantUserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        initMyProfileListRecyclerView(binding.rvMyProfileList)
        setupCalendar()
        initRelevantUserListRecyclerView(binding.rvRelevantUsers)
    }

    private fun setupListeners() {
        binding.cvAddMyProfile.setOnClickListener {
            createNewProfile()
        }

        binding.ivAlarm.setOnClickListener {

        }

        binding.ivSetting.setOnClickListener {

        }

        binding.btnPost.setOnClickListener {
            intentPostActivity()
        }
    }

    private fun initMyProfileListRecyclerView(recyclerView: RecyclerView) {
        myProfileListAdapter = MyProfileListAdapter()

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = myProfileListAdapter
//            myProfileListAdapter.submitList(
//                listOf(
//                    MyProfileData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    MyProfileData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    MyProfileData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    MyProfileData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    MyProfileData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    )
//                )
//            )
        }
    }

    private fun setupCalendar() {
        binding.calendarView.setTitleFormatter { day -> "${day!!.year}년 ${day.month}월" }
        val calendarMin = Calendar.getInstance()
        calendarMin.add(Calendar.DAY_OF_MONTH, -30)
        val calendarMax = Calendar.getInstance()
        calendarMax.add(Calendar.DAY_OF_MONTH, +30)
//        binding.calendarView.setOnSampleReceivedEvent(this)
//        binding.calendarView.setMinimumDate(calendarMin)
//        binding.calendarView.setMaximumDate(calendarMax)
    }

    private fun initRelevantUserListRecyclerView(recyclerView: RecyclerView) {
        relevantUserListAdapter = RelevantUserListAdapter(
            userProfileClick = ::intentUserProfile
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = relevantUserListAdapter
//            relevantUserListAdapter.submitList(
//                listOf(
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    ),
//                    RelevantUserData(
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"
//                    )
//                )
//            )
        }
    }

    private fun intentUserProfile() {
//        val intent = UserProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun createNewProfile() {
//        val intent = CreateNewProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun intentPostActivity() {
//        val intent =  PostingAddActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

class CurrentDayDecorator(context: Context, currentDay: CalendarDay) : DayViewDecorator {
    private val drawable: Drawable?
    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector)
    }
}

class EventDecorator() : DayViewDecorator {

    private lateinit var dates : HashSet<CalendarDay>
    private lateinit var drawableValue: Drawable
    private var color = 0

    constructor(context: Context, color: Int, dates: Collection<CalendarDay>) : this() {
        drawableValue = ContextCompat.getDrawable(context, R.drawable.calendar_background)!!
        this.color = color
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10F, color))
        view?.setSelectionDrawable(drawableValue)
    }
}
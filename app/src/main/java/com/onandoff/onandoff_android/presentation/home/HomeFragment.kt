package com.onandoff.onandoff_android.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.onandoff.onandoff_android.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment: Fragment(), OnDayClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding
            get() = _binding!!

    private lateinit var myProfileListAdapter: MyProfileListAdapter
    private lateinit var relevantUserListAdapter: RelevantUserListAdapter

    private val datePicker
        = MaterialDatePicker.Builder.datePicker()
        .setTitleText("")
        .build()

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
        initRelevantUserListRecyclerView(binding.rvRelatedUsers)
    }

    private fun setupListeners() {
        binding.ivAlarm.setOnClickListener {

        }

        binding.ivSetting.setOnClickListener {

        }

        binding.btnPost.setOnClickListener {
            intentPostActivity()
        }
    }

    private fun initMyProfileListRecyclerView(recyclerView: RecyclerView) {
        myProfileListAdapter = MyProfileListAdapter(
            addProfileClick = ::createNewProfile
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = myProfileListAdapter
//            myProfileListAdapter.submitList(listOf(MyProfileData(
//                profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", name = "David"),
//                MyProfileData(isAlreadyAdded=true))
//            )
        }
    }

    private fun setupCalendar() {
//        binding.calendar.setTitleFormatter { day -> "${day!!.year}년 ${day.month}월" }
        val calendarMin = Calendar.getInstance()
        calendarMin.add(Calendar.DAY_OF_MONTH, -30)
        val calendarMax = Calendar.getInstance()
        calendarMax.add(Calendar.DAY_OF_MONTH, +30)
//        binding.calendarView.setOnSampleReceivedEvent(this)
        binding.calendarView.setMinimumDate(calendarMin)
        binding.calendarView.setMaximumDate(calendarMax)
    }

    override fun onDayClick(eventDay: EventDay) {
        TODO("Not yet implemented")
    }

    private fun initRelevantUserListRecyclerView(recyclerView: RecyclerView) {
        relevantUserListAdapter = RelevantUserListAdapter(
            userProfileClick = ::intentUserProfile
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = relevantUserListAdapter
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
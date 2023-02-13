package com.onandoff.onandoff_android.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.MyPersonaData
import com.onandoff.onandoff_android.data.model.RelevantUserData
import com.onandoff.onandoff_android.databinding.FragmentHomeBinding
import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingAddActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment: Fragment(), CalendarAdapter.OnMonthChangeListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding
            get() = _binding!!

    private lateinit var myProfileListAdapter: MyProfileListAdapter
    private lateinit var relevantUserListAdapter: RelevantUserListAdapter
    private lateinit var calendarAdapter: CalendarAdapter

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
        myProfileListAdapter = MyProfileListAdapter(
            onClick = ::getMyPersona
        )

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

    private fun getMyPersona(myPersonaData: MyPersonaData) {
        // TODO: 2023-01-21 데이터 연동 추가하기
        binding.tvUserPersona1.text = myPersonaData.name
        binding.tvUserName1.text = myPersonaData.name

        binding.tvUserPersona2.text = myPersonaData.name
        binding.tvUserName2.text = myPersonaData.name
    }

    private fun setupCalendar() {
        val baseCalendar = BaseCalendar()

        calendarAdapter = CalendarAdapter(this)
        binding.fgCalDay.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        binding.fgCalDay.adapter = calendarAdapter

        binding.fgCalPre.setOnClickListener {
            calendarAdapter.changeToPrevMonth()
        }
        binding.fgCalNext.setOnClickListener {
            calendarAdapter.changeToNextMonth()
        }

        baseCalendar.initBaseCalendar {
            onMonthChanged(it)
        }


//        val calendarMin = Calendar.getInstance()
//        calendarMin.add(Calendar.DAY_OF_MONTH, -30)
//        val calendarMax = Calendar.getInstance()
//        calendarMax.add(Calendar.DAY_OF_MONTH, +30)
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

    private fun intentUserProfile(relevantUserData: RelevantUserData) {
//        val intent = UserProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun createNewProfile() {
//        val intent = CreateNewProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun intentPostActivity() {
        val intent = Intent(requireActivity(), PostingAddActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMonthChanged(calendar: Calendar) {
        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        binding.fgCalMonth.text = sdf.format(calendar.time)
    }
}
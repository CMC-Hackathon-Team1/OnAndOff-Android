package com.onandoff.onandoff_android.presentation.home

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.CreateMyProfileData
import com.onandoff.onandoff_android.data.model.MyProfileResponse

import androidx.recyclerview.widget.GridLayoutManager
import com.onandoff.onandoff_android.data.api.feed.CalendarService
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.CalendarData

import com.onandoff.onandoff_android.data.model.RelevantUserData
import com.onandoff.onandoff_android.data.model.StatisticsResponse
import com.onandoff.onandoff_android.databinding.FragmentHomeBinding

import com.onandoff.onandoff_android.presentation.home.persona.CreatePersonaActivity
import com.onandoff.onandoff_android.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingAddActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.*

class HomeFragment: Fragment(), CalendarAdapter.OnMonthChangeListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding
            get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>(factoryProducer = {
        HomeViewModel.Factory
    })

    private val myProfileListAdapter: MyProfileListAdapter by lazy {
        MyProfileListAdapter(
            onClick = ::onClickPersona
        )
    }
    private lateinit var relevantUserListAdapter: RelevantUserListAdapter
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
        setupListeners()
        viewModel.getMyPersonaList()
    }

    private fun setupView() {
        initMyPersonaListRecyclerView(binding.rvMyProfileList)
        setupCalendar()
//        initRelevantUserListRecyclerView(binding.rvRelevantUsers)
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is HomeViewModel.State.GetPersonaFailed -> {
                            when (state.reason) {
                                HomeViewModel.State.GetPersonaFailed.Reason.PARAMETER_ERROR -> {
                                    Toast.makeText(requireActivity(), "parameter error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(requireActivity(), "jwt error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(requireActivity(), "db error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaFailed.Reason.NO_PROFILE -> {
                                    Toast.makeText(requireActivity(), "no profile", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is HomeViewModel.State.GetPersonaListFailed -> {
                            when (state.reason) {
                                HomeViewModel.State.GetPersonaListFailed.Reason.PARAMETER_ERROR -> {
                                    Toast.makeText(requireActivity(), "parameter error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaListFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(requireActivity(), "jwt error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaListFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(requireActivity(), "db error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaListFailed.Reason.NO_PROFILE -> {
                                    Toast.makeText(requireActivity(), "no profile", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetPersonaListFailed.Reason.NOT_MY_PROFILE -> {
                                    Toast.makeText(requireActivity(), "not my profile", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is HomeViewModel.State.GetMonthlyCountFailed -> {
                            when (state.reason) {
                                HomeViewModel.State.GetMonthlyCountFailed.Reason.NO_PROFILE_ID_OR_INVALID_VALUE -> {
                                    Toast.makeText(requireActivity(), "no profile or invalid value error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetMonthlyCountFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(requireActivity(), "jwt error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetMonthlyCountFailed.Reason.SERVER_ERROR -> {
                                    Toast.makeText(requireActivity(), "server error", Toast.LENGTH_SHORT).show()
                                }
                                HomeViewModel.State.GetMonthlyCountFailed.Reason.JWT_TOKEN_AND_PROFILE_NOT_SAME -> {
                                    Toast.makeText(requireActivity(), "jwt token and profile are not same", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        HomeViewModel.State.Idle -> {
                        }
                        HomeViewModel.State.GetPersonaSuccess -> {
                            binding.tvUserPersona1.text = personaName.value.toString()
                            binding.tvUserName1.text = profileName.value.toString()
                        }
                        is HomeViewModel.State.GetPersonaListSuccess -> {
                            Log.d("state.myProfileList", "${state.myProfileList}")
                            myProfileListAdapter.submitList(state.myProfileList.result)

//                            val firstProfile = state.myProfileList.result?.getOrNull()
                            val firstProfile = state.myProfileList.result?.firstOrNull()
                            if (firstProfile != null) {
                                onClickPersona(firstProfile)
                            }
                        }
                        is HomeViewModel.State.GetMonthlyCountSuccess -> {
                            binding.tvMonthlyLikesCount.text = "${state.monthlyLikesCount}"
                            binding.tvMonthlyMyFeedsCount.text = "${state.monthMyFeedsCount}"
                            binding.tvMonthlyMyFollowersCount.text = "${state.monthlyMyFollowersCount}"
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.cvAddMyProfile.setOnClickListener {
            if (myProfileListAdapter.currentList.size < 3) {
                createNewProfile()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "프로필은 3개까지 생성 가능합니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.ivAlarm.setOnClickListener {

        }

        binding.ivSetting.setOnClickListener {

        }

        binding.btnPost.setOnClickListener {
            intentPostActivity()
        }
    }

    private fun initMyPersonaListRecyclerView(recyclerView: RecyclerView) {
        recyclerView.run {
            adapter = myProfileListAdapter
            val spaceDecoration = HorizontalSpaceItemDecoration(25)
            removeItemDecoration(object : DividerItemDecoration(requireActivity(), HORIZONTAL) {

            })
            addItemDecoration(spaceDecoration)
        }
    }

    // RecyclerView Item 간 간격 조정하기 위한 클래스
    inner class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = state.itemCount

            when (position) {
                0 -> {
                    outRect.left = 0
                }
                else -> {
                    outRect.left = horizontalSpaceWidth
                }
            }
        }
    }

    private fun onClickPersona(profileResponse: MyProfileResponse) {
        // TODO: 화면에 표시될 페르소나 데이터 연동 추가하기
        binding.tvUserPersona1.text = profileResponse.personaName
        binding.tvUserName1.text = profileResponse.profileName

        binding.tvUserPersona2.text = profileResponse.personaName
        binding.tvUserName2.text = profileResponse.profileName

        viewModel.setSelectedProfile(profileResponse)
    }

    /**
    * 순서 :  1,2,3,4,5,6,7,8
    * 1, 2 = topLeftRadius
    * 3, 4 = topRightRadius
    * 5, 6 = bottomRightRadius
    * 7, 8 = bottomLeftRadius
    * */
    fun makeRoundedRectangleDrawable(
        radius: Float = 24f,
        strokeWidth: Int = 1,
        strokeColor: Int,
        backgroundColor: Int? = null
    ): Drawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setStroke(strokeWidth, strokeColor)
        backgroundColor?.run { setColor(backgroundColor) }
        cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    }



    private fun getMonthlyStatistics() {
        // TODO: 월 별 작성한 공감, 게시글, 팔로워 수 데이터 연동 추가하기
//        binding.tvMonthlyLikesCount.text
//        binding.tvMonthlyMyFeedsCount.text
//        binding.tvMonthlyFollowersCount.text
    }

    private fun getMonthlyLikes(statisticsResponse: StatisticsResponse) {
        // TODO: 월 별 공감 수 데이터 연동 표시하기
//        binding.tvMonthlyLikesCount.text
    }

    private fun getMonthlyMyFeeds(statisticsResponse: StatisticsResponse) {
        // TODO: 월 별 작성한 게시글 수 데이터 연동 표시하기
//        binding.tvMonthlyLikesCount.text
    }

    private fun getMonthlyFollowers(statisticsResponse: StatisticsResponse) {
        // TODO: 월 별 팔로워 수 데이터 연동 표시하기
//        binding.tvMonthlyFollowersCount.text
    }


    private fun setupCalendar() {
        val baseCalendar = BaseCalendar()

        baseCalendar.initBaseCalendar {
            onMonthChanged(it)
        }

        calendarAdapter = CalendarAdapter(this)
        binding.fgCalDay.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        binding.fgCalDay.adapter = calendarAdapter

        binding.fgCalPre.setOnClickListener {
            calendarAdapter.changeToPrevMonth()
        }
        binding.fgCalNext.setOnClickListener {
            calendarAdapter.changeToNextMonth()
        }
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

    private fun intentUserProfile(relevantUserData: RelevantUserData) {
//        val intent = UserProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun createNewProfile() {
        val intent = CreatePersonaActivity.getIntent(requireActivity())
        startActivity(intent)
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
        val userId = 27
        val year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) + 1
        var month_format = month.toString()
        if (month < 10) {
            month_format = "0${month_format}"
        }

        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        binding.fgCalMonth.text = sdf.format(calendar.time)

        val calendarInterface: CalendarService? = RetrofitClient.getClient()?.create(CalendarService::class.java)
        val call = calendarInterface?.getCalendarList(userId, year, month_format)
        call?.enqueue(object : Callback<List<CalendarData>> {
            override fun onResponse(
                call: Call<List<CalendarData>>,
                response: Response<List<CalendarData>>
            ) {
                when(response.code()) {
                    200 -> {
                        val feedList = response.body()
                        Log.d("feedList", "onResponse: ${feedList?.size}")
                        calendarAdapter.setItems(feedList!!)
                    }
                }
            }

            override fun onFailure(call: Call<List<CalendarData>>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("TAG", "onFailure: caledar error")
            }

        })
    }
}
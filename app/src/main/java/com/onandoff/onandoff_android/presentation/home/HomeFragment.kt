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

import androidx.recyclerview.widget.GridLayoutManager
import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*

import com.onandoff.onandoff_android.databinding.FragmentHomeBinding

import com.onandoff.onandoff_android.presentation.home.persona.CreatePersonaActivity
import com.onandoff.onandoff_android.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingAddActivity
import com.onandoff.onandoff_android.presentation.home.posting.PostingReadActivity
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.*

class HomeFragment: Fragment(), CalendarAdapter.OnMonthChangeListener, CalendarAdapter.OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding
            get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>(factoryProducer = {
        HomeViewModel.Factory
    })

    private lateinit var myProfileListAdapter: MyProfileListAdapter
    private lateinit var relevantUserListAdapter: RelevantUserListAdapter
    private lateinit var calendarAdapter: CalendarAdapter
    private var profileId: Int? = null

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
                        is HomeViewModel.State.GetPersonaSuccess -> {
                            binding.tvUserPersona1.text = personaName.value.toString()
                            binding.tvUserName1.text = profileName.value.toString()
                            Log.d("state.myProfile.isSelected : ", "${state.myProfile.isSelected}")
//                            setUserName(state.myProfile)
                        }
                        is HomeViewModel.State.GetPersonaListSuccess -> {
                            Log.d("state.myProfileList", "${state.profileList}")
                            Log.d("selectedProfile", "selectedProfile: $selectedProfile")
                            myProfileListAdapter.submitList(state.profileList)

                            val selectedProfile = state.profileList.find { it.isSelected }

                            if (selectedProfile != null) {
                                Log.d("selectedProfile", "selectedProfile.isSelected: ${selectedProfile.isSelected}")
                                setUserName(selectedProfile)
                            }
                        }
                        is HomeViewModel.State.GetMonthlyCountSuccess -> {
                            binding.tvMonthlyLikesCount.text = "${state.monthlyLikesCount}"
                            binding.tvMonthlyMyFeedsCount.text = "${state.monthMyFeedsCount}"
                            binding.tvMonthlyMyFollowersCount.text = "${state.monthlyMyFollowersCount}"
                        }
                        is HomeViewModel.State.GetCalendarFeedListSuccess -> {
                            calendarAdapter.setItems(state.calendarList)
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.cvAddMyProfile.setOnClickListener {
            if (myProfileListAdapter.currentList.size < 5) {
                createNewProfile()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "프로필은 5개까지 생성 가능합니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.ivSetting.setOnClickListener {
            val intent = SettingActivity.getIntent(requireActivity())
            startActivity(intent)
        }

        binding.btnPost.setOnClickListener {
            intentPostActivity()
        }
    }

    private fun initMyPersonaListRecyclerView(recyclerView: RecyclerView) {
        myProfileListAdapter = MyProfileListAdapter(
            onClick = ::onClickPersona
        )

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

    private fun onClickPersona(item: MyProfileItem) {
//        item.isSelected = true
        viewModel.setSelectedProfile(item)
        Log.d("onClickPersona", "onClickPersona: ${item.isSelected}") // 여기에서 false 로 표시됨
    }

    private fun setUserName(item: MyProfileItem) {
        binding.tvUserPersona1.text = item.myProfile.personaName
        binding.tvUserName1.text = item.myProfile.profileName

        binding.tvUserPersona2.text = item.myProfile.personaName
        binding.tvUserName2.text = item.myProfile.profileName
        profileId = item.myProfile.profileId
        prefs.putSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID, profileId!!)
        viewModel.setSelectedProfile(item)
        Log.d("setUserName", "setUserName: : ${item.isSelected}")
    }

    private fun setupCalendar() {
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

    private fun initRelevantUserListRecyclerView(recyclerView: RecyclerView) {
        relevantUserListAdapter = RelevantUserListAdapter(
            userProfileClick = ::onClickOtherUserProfile
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

    private fun onClickOtherUserProfile(relevantUserData: RelevantUserData) {
//        val intent = UserProfileActivity.getIntent(requireActivity())
//        startActivity(intent)
    }

    private fun createNewProfile() {
        val intent = CreatePersonaActivity.getIntent(requireActivity())
        startActivity(intent)
    }

    private fun intentPostActivity() {
        val intent = Intent(requireActivity(), PostingAddActivity::class.java)
        intent.putExtra("profileId", profileId)
        startActivity(intent)
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

        val calendarInterface: CalendarInterface? = RetrofitClient.getClient()?.create(CalendarInterface::class.java)
        val call = calendarInterface?.getCalendarList(userId, year, monthFormat)
        call?.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(
                call: Call<CalendarResponse>,
                response: Response<CalendarResponse>
            ) {
                Log.d("feedList", "onResponse: ${response.code()}")

                when(response.code()) {
                    200 -> {
                        val feeds = response.body()?.result
                        Log.d("feedList", "onResponse: ${feeds?.size}")
                        if (!feeds.isNullOrEmpty()) {
                            calendarAdapter.setItems(feeds)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: calendar error ${t.message}")
            }

        })
    }

    override fun onClick(v: View, position: Int, feedId: Int) {
        intentPostReadActivity(feedId)
    }

    private fun intentPostReadActivity(feedId: Int) {
        //intent로 profileId랑 feedId를 보내야함
        val intent = Intent(requireActivity(), PostingReadActivity::class.java)
        intent.putExtra("profileId", profileId)
        intent.putExtra("feedId", feedId)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
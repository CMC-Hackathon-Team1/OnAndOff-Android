package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.databinding.FragmentOtherUserBinding
import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.persona.CreatePersonaDialog
import com.onandoff.onandoff_android.presentation.home.posting.PostingReadActivity
import com.onandoff.onandoff_android.presentation.look.BottomSheetLookAroundFeedOptionMenu
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "OtherUserFragment"

class OtherUserFragment : Fragment(), CalendarAdapter.OnMonthChangeListener,
    CalendarAdapter.OnItemClickListener, OtherUserFeedListAdapter.OnItemClickListener {
    private var _binding: FragmentOtherUserBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by activityViewModels<BlockOtherUserViewModel>(factoryProducer = {
        BlockOtherUserViewModel.Factory
    })

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var otherUserFeedListAdapter: OtherUserFeedListAdapter

    private var profileId by Delegates.notNull<Int>()
    private var otherUserId by Delegates.notNull<Int>()
    private var feedList = ArrayList<FeedResponseData>()
    private var page: Int = 1
    private var yearFeed: Int = 2023
    private var monthFeed: String = "03"

    private val feedService = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherUserBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileId = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID, 0
        )
        otherUserId = if (arguments?.getInt(PROFILE_ID) != null) {
            arguments?.getInt(PROFILE_ID)!!
        } else {
            -1
        }
        setupCalendar()
        setupFollowButton()
        setupFollowStatus()
        getProfileData()
        onInitRecyclerView()
        setupViewModel()

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.ivOptionMenu.setOnClickListener {
            val bottomSheet = BottomSheetBlockOtherUserOptionMenu.newInstance(otherUserId)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is BlockOtherUserViewModel.State.BlockOtherUserFailed -> {
                            when (state.reason) {
                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.ALREADY_BLOCKED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "already blocked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.INVALID_FROM_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid from profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                BlockOtherUserViewModel.State.BlockOtherUserFailed.Reason.INVALID_TO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid to profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is BlockOtherUserViewModel.State.Idle -> {}
                        is BlockOtherUserViewModel.State.BlockOtherUserSuccess -> {
                            val blockOtherUserConfirmedDialog = BlockOtherUserConfirmedDialog.newInstance()
                            blockOtherUserConfirmedDialog.show(childFragmentManager, BlockOtherUserConfirmedDialog.TAG)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMonthChanged(calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        var monthFormat = month.toString()
        if (month < 10) {
            monthFormat = "0${monthFormat}"
        }

        page = 1
        getFeedData(year, monthFormat)

        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        binding.fgCalMonth.text = sdf.format(calendar.time)

        val calendarInterface: CalendarInterface? = RetrofitClient.getClient()?.create(
            CalendarInterface::class.java
        )
        val call = calendarInterface?.getCalendarList(otherUserId, year, monthFormat)
        call?.enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(
                call: Call<CalendarResponse>, response: Response<CalendarResponse>
            ) {
                Log.d("feedList", "onResponse: ${response.code()}")

                when (response.code()) {
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

    private fun getProfileData() {
        val profileService: ProfileInterface? = RetrofitClient.getClient()?.create(
            ProfileInterface::class.java
        )
        val call = profileService?.getMyProfile(otherUserId)
        call?.enqueue(object : Callback<getMyProfileResponse> {
            override fun onResponse(
                call: Call<getMyProfileResponse>, response: Response<getMyProfileResponse>
            ) {
                binding.profile2 = response.body()?.result!!
                Log.d(TAG, "onResponse: ${response.body()?.result!!}")

            }

            override fun onFailure(call: Call<getMyProfileResponse>, t: Throwable) {

            }
        })
    }

    private fun setupFollowStatus() {
        val request = FollowRequest(profileId, otherUserId)
        val call = feedService?.followStatusResponse(request)
        call?.enqueue(object : Callback<LikeFollowResponse> {
            override fun onResponse(
                call: Call<LikeFollowResponse>, response: Response<LikeFollowResponse>
            ) {
                if (response.body()?.message == "Follow") {
                    binding.followingBtn.setImageResource(R.drawable.ic_is_following)
                } else {
                    binding.followingBtn.setImageResource(R.drawable.ic_not_following)
                }
            }

            override fun onFailure(call: Call<LikeFollowResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setupFollowButton() {
        binding.followingBtn.setOnClickListener {
            val request = FollowRequest(profileId, otherUserId)
            val call = feedService?.followResponse(request)
            call?.enqueue(object : Callback<LikeFollowResponse> {
                override fun onResponse(
                    call: Call<LikeFollowResponse>, response: Response<LikeFollowResponse>
                ) {
                    if (response.body()?.message == "Follow") {
                        binding.followingBtn.setImageResource(R.drawable.ic_is_following)
                    } else {
                        binding.followingBtn.setImageResource(R.drawable.ic_not_following)
                    }
                }

                override fun onFailure(call: Call<LikeFollowResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter(this)
        calendarAdapter.setItemClickListener(this)
        binding.fgCalDay.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        binding.fgCalDay.adapter = calendarAdapter

        binding.fgCalPre.setOnClickListener {
            feedList.clear()
            otherUserFeedListAdapter.removeItems()
            calendarAdapter.changeToPrevMonth()
        }
        binding.fgCalNext.setOnClickListener {
            feedList.clear()
            otherUserFeedListAdapter.removeItems()
            calendarAdapter.changeToNextMonth()
        }
    }

    override fun onClick(v: View, position: Int, feedId: Int) {
        intentPostReadActivity(feedId)
    }

    private fun intentPostReadActivity(feedId: Int) {
        val intent = Intent(requireActivity(), PostingReadActivity::class.java)
        intent.putExtra("otherUserId", otherUserId)
        intent.putExtra("profileId", profileId)
        intent.putExtra("feedId", feedId)
        startActivity(intent)
    }

    private fun onInitRecyclerView() {
        otherUserFeedListAdapter = OtherUserFeedListAdapter(feedList)
        binding.rvFeedList.adapter = otherUserFeedListAdapter
        binding.rvFeedList.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true
        )
        otherUserFeedListAdapter.setItemClickListener(this)
        binding.rvFeedList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var itemCount = binding.rvFeedList.adapter?.itemCount

                if (itemCount != null && itemCount == 10) {
                    if ((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == itemCount - 1) {
                        page += 1
                        getFeedData(yearFeed, monthFeed)
                    }
                }
            }
        })
    }

    private fun getFeedData(year: Int, month: String) {
        yearFeed = year
        monthFeed = month
        val call =
            feedService?.getOtherUserFeedListResponse(profileId, otherUserId, year, month, page)
        call?.enqueue(object : Callback<getFeedListRespone> {
            override fun onResponse(
                call: Call<getFeedListRespone>, response: Response<getFeedListRespone>
            ) {
                Log.d(TAG, "onResponse: ${response.code()}")
                Log.d(TAG, "onResponse: ${response.body()?.result}")
                val feedArray = response.body()?.result?.feedArray
                if (feedArray != null) {
                    otherUserFeedListAdapter.setItems(feedArray)
                }
            }

            override fun onFailure(call: Call<getFeedListRespone>, t: Throwable) {
                Log.d(TAG, "fail")
            }
        })
    }


    companion object {
        private const val FEED_ID = "feed_id"
        private const val PROFILE_ID = "profile_id"

        fun newInstance(feedId: Int, profileId: Int): OtherUserFragment =
            OtherUserFragment().apply {
                arguments = Bundle().apply {
                    putInt(FEED_ID, feedId)
                    putInt(PROFILE_ID, profileId)
                }
            }
    }

    override fun onClick(v: View, feedId: Int) {
        val bottomSheetDialogFragment =
            BottomSheetLookAroundFeedOptionMenu.newInstance(feedId = feedId)
        bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
    }
}

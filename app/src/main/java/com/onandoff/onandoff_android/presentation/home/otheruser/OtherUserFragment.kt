package com.onandoff.onandoff_android.presentation.home.otheruser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.feed.MyFeedService
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.databinding.FragmentOtherUserBinding
import com.onandoff.onandoff_android.presentation.home.calendar.BaseCalendar
import com.onandoff.onandoff_android.presentation.home.calendar.CalendarAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingReadActivity
import com.onandoff.onandoff_android.presentation.mypage.MypageRVAdapter
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

private const val TAG = "OtherUserFragment"

class OtherUserFragment : Fragment(), CalendarAdapter.OnMonthChangeListener,
    CalendarAdapter.OnItemClickListener {
    private var _binding: FragmentOtherUserBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var otherUserFeedListAdapter: OtherUserFeedListAdapter

    private var profileId by Delegates.notNull<Int>()
    private var otherUserId by Delegates.notNull<Int>()
    private var feedList = ArrayList<FeedResponseData>()

    private val feedService = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileId = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID, 0
        )
        otherUserId = 27
        setupCalendar()
        setupFollowButton()
        getProfileData()
        getFeedData()
        onInitRecyclerView()
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
        val call = profileService?.getMyProfile(profileId)
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

    private fun setupFollowButton() {
        binding.followingBtn.setOnClickListener {
            val request = FollowRequest(profileId, otherUserId)
            val call = feedService?.followResponse(request)
            call?.enqueue(object : Callback<LikeFollowResponse> {
                override fun onResponse(
                    call: Call<LikeFollowResponse>, response: Response<LikeFollowResponse>
                ) {
                    if(response.body()?.message == "Follow") {
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
    }

    private fun getFeedData() {
        val call = feedService?.getOtherUserFeedListResponse(profileId, otherUserId, 2023, "03", 1)
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
}
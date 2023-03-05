package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.MyFeedService
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.databinding.FragmentMypageBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MypageFragment: Fragment(){
    private lateinit var binding: FragmentMypageBinding
    private var writeList = ArrayList<MyPosting>()
    private val TAG = "Mypage"
    lateinit var profile:ProfileListResultResponse
    lateinit var mainActivity: MainActivity
    var feedList = ArrayList<FeedResponseData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
//        setupListeners()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }
    private fun setupView(){

        getProfileData()
        getFeedData()
        onInitRecyclerView()
        binding.tvMypageEdit.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("nickName", profile.profileName)
            bundle.putString("personaName", profile.personaName)
            bundle.putString("profileImg", profile.profileImgUrl)
            bundle.putString("statusMsg", profile.statusMessage)
            val editFragemnt = ProfileEditFragment()
            editFragemnt.arguments = bundle
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main,editFragemnt)
                .commit()
        }
    }
    fun getProfileData(){
        val profileService: ProfileInterface? = RetrofitClient.getClient()?.create(
            ProfileInterface::class.java)
        val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
        //날을 어떻게 넣을지 고민해봐야될듯
        val call = profileService?.getMyProfile(profileId)
        call?.enqueue(object: Callback<getMyProfileResponse> {
            override fun onResponse(
                call: Call<getMyProfileResponse>,
                response: Response<getMyProfileResponse>
            ){
                Log.d(TAG,"api 호출")
                profile = response.body()?.result!!
                binding.profile = profile

            }
            override fun onFailure(call: Call<getMyProfileResponse>, t: Throwable){

            }
        })
    }
    private fun getFeedData(){
        val myfeedService: MyFeedService? = RetrofitClient.getClient()?.create(
            MyFeedService::class.java)
        val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
        //날을 어떻게 넣을지 고민해봐야될듯
        val call = myfeedService?.getMyFeed(profileId,profileId,2023, "03",1)
        call?.enqueue(object: Callback<getFeedResponeData> {
            override fun onResponse(
                call: Call<getFeedResponeData>,
                response: Response<getFeedResponeData>
            ){

                val tmpFeedList = response.body()?.result
                if (tmpFeedList != null) {
                    for(item in tmpFeedList){
                        feedList.apply{add(item)}
                    }
                }
            }
            override fun onFailure(call: Call<getFeedResponeData>, t: Throwable){

            }
        })
    }
    private fun onInitRecyclerView(){
        val mypageRVAdapter =MypageRVAdapter(feedList, mainActivity)
        binding.rvProfileList.adapter = mypageRVAdapter;
        binding.rvProfileList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
    }
}

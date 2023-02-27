package com.onandoff.onandoff_android.presentation.mypage

import android.content.Intent
import android.os.Bundle
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
import com.onandoff.onandoff_android.data.model.FeedResponseData
import com.onandoff.onandoff_android.data.model.MyPosting
import com.onandoff.onandoff_android.data.model.ProfileResponse
import com.onandoff.onandoff_android.databinding.FragmentMypageBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment: Fragment(){
    private lateinit var binding: FragmentMypageBinding
    private var writeList = ArrayList<MyPosting>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
//        setupListeners()
    }
    private fun setupView(){
//        binding.tvMypageEdit.setOnClickListener{
//            val editFragemnt = ProfileEditFragment()
//            supportFragmentManager()
//                .beginTransaction()
//                .add(R.id.framelayout, editFragemnt)
//                .commit()
//        }
        getData()
        onInitRecyclerView()



    }
    private fun getData(){
        val myfeedService: MyFeedService? = RetrofitClient.getClient()?.create(
            MyFeedService::class.java)
        val profileId = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)
        //날을 어떻게 넣을지 고민해봐야될듯
        val call = myfeedService?.getMyFeed(profileId,2023,1,0)
        call?.enqueue(object: Callback<FeedResponseData> {
            override fun onResponse(
                call: Call<FeedResponseData>,
                response: Response<FeedResponseData>
            ){
//                val size:Int = response.body()

            }
            override fun onFailure(call: Call<FeedResponseData>, t: Throwable){

            }
        })
    }


    private fun onInitRecyclerView(){
        addDummy()
        val mypageRVAdapter = MypageRVAdapter(writeList)
        binding.rvProfileList.adapter = mypageRVAdapter;
        binding.rvProfileList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

    }

    private fun addDummy() {
        writeList.apply {
            add(
                MyPosting(
                    1,
                    1,
                    "4",
                    1,
                    2,
                    "",
                    "으악",
                    "아크",
                    "디자이너",
                    "독서의 계절.. ㅎㅎ",
                    0,
                    "2022/09/25",
                    "#개발 #기획 #디자인"
                )
            )
            add(
                MyPosting(
                    2,
                    1,
                    "40",
                    1,
                    2,
                    "",
                    "으악",
                    "아크",
                    "디자이너",
                    "너무 졸려요",
                    0,
                    "2022/09/24",
                    "#개발 #기획 #디자인"
                )
            )
            add(
                MyPosting(
                    1,
                    1,
                    "12",
                    1,
                    2,
                    "",
                    "으악",
                    "아크",
                    "디자이너",
                    "안드로이드 화이팅",
                    0,
                    "2022/09/23",
                    "#개발 #기획 #디자인"
                )
            )
            add(
                MyPosting(
                    1,
                    1,
                    "8",
                    1,
                    2,
                    "",
                    "으악",
                    "아크",
                    "디자이너",
                    "서버 화이팅",
                    0,
                    "2022/09/22",
                    "#개발 #기획 #디자인"
                )
            )

        }
    }

}

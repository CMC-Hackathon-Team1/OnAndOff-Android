package com.onandoff.onandoff_android.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.onandoff.onandoff_android.data.model.MyPosting
import com.onandoff.onandoff_android.databinding.FragmentMypageBinding

class MypageFragment: Fragment(){

    private lateinit var binding : FragmentMypageBinding
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
        onInitRecyclerView()



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
package com.onandoff.onandoff_android.presentation.look

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.onandoff.onandoff_android.LookAroundData
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentLookAroundBinding

class LookAroundFragment : Fragment() {
    private var _binding: FragmentLookAroundBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var lookAroundListAdapter: LookAroundListAdapter
    private val searchLookAroundList = mutableListOf<LookAroundData>()
    private val followingLookAroundList = mutableListOf<LookAroundData>()

    private val items = activity?.resources?.getStringArray(R.array.look_category_array)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookAroundBinding.inflate(inflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        initLookAroundTabs()
        initSpinner()
        initLookAroundRecyclerView(binding.rvLookAround)
    }

    private fun initLookAroundTabs() {
        binding.tlLookAroundList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        lookAroundListAdapter.setLookAroundListOnTab(searchLookAroundList)
                        lookAroundListAdapter.notifyDataSetChanged()
                    }
                    1 -> {
                        lookAroundListAdapter.setLookAroundListOnTab(followingLookAroundList)
                        lookAroundListAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initSpinner() {
        val spinnerAdapter = object : ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items.orEmpty()) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {

                } else {

                }
                return view
            }
        }

        // Set drop down view resource and attach the adapter to your spinner.
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // 아이템을 추가
        spinnerAdapter.addAll(items.orEmpty().toMutableList())
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.setSelection(spinnerAdapter.count)

        //스피너 선택시 나오는 화면
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 아이템이 클릭되면 맨 위(position 0번)부터 순서대로 동작하게 된다.
                val value = parent!!.getItemAtPosition(position).toString()
                when (position) {
                    0 -> {
                        getLookAroundList(0)
                    }
                    1 -> {
                        getLookAroundList(1)
                    }
                    2 -> {
                        getLookAroundList(2)
                    }
                    else -> {
                        getLookAroundList(3)
                    }
                }
            }

            override fun onNothingSelected(view: AdapterView<*>?) {
                return
            }
        }
    }

    private fun getLookAroundList(position: Int) {
        // TODO: 카테고리에 따라 데이터를 불러오는 API 연동하기
    }


    private fun initLookAroundRecyclerView(recyclerView: RecyclerView) {
        lookAroundListAdapter = LookAroundListAdapter(
            onClick = ::intentPost,
            onFollowClick = ::addToFollowingList,
            onLikeClick = ::addLike
        )

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = lookAroundListAdapter
        }
    }

    private fun intentPost(lookAroundData: LookAroundData) {
        // TODO: 해당 데이터의 상세 페이지로 이동하기
//        val intent = UserPostActivity.getIntent(requireActivity(), lookAroundData.key)
//        startActivity(intent)
    }

    private fun addToFollowingList(lookAroundData: LookAroundData) {
        // TODO: 해당 데이터를 팔로잉 목록에 추가하기
    }

    private fun addLike(lookAroundData: LookAroundData) {
        // TODO: 해당 데이터에 좋아요 개수를 +1 하기
    }

    companion object {
        fun newInstance() = LookAroundFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
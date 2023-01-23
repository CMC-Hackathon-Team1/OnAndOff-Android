package com.onandoff.onandoff_android.presentation.look

import android.os.Bundle
import android.util.TypedValue
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
import com.onandoff.onandoff_android.data.model.LookAround
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentLookAroundBinding

class LookAroundFragment : Fragment() {
    private var _binding: FragmentLookAroundBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var lookAroundListAdapter: LookAroundListAdapter
    private val searchLookAroundList = mutableListOf<LookAround>()
    private val followingLookAroundList = mutableListOf<LookAround>()

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
        val items = activity?.resources?.getStringArray(R.array.look_category_array)

        val spinnerAdapter = object : ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_spinner
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view = super.getView(position, convertView, parent)

                if (position == count) {
                    // 마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (view.findViewById<View>(R.id.tv_item_spinner) as TextView).text = ""
                    // 아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (view.findViewById<View>(R.id.tv_item_spinner) as TextView).hint = getItem(count)
                }

                return view
            }

            override fun getCount(): Int {
                // 마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter.addAll(items!!.toMutableList()) // 아이템을 추가
        spinnerAdapter.add("카테고리 전체")
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.setSelection(spinnerAdapter.count)
        binding.spinner.dropDownVerticalOffset = dipToPixels(55f).toInt()

        // 스피너 선택시 나오는 화면
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

    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
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
//            lookAroundListAdapter.submitList(
//                listOf(
//                    LookAroundData(
//                        key = "",
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
//                        name = "David",
//                        postDate = listOf("2023", "01", "30"),
//                        isFollowing = false,
//                        like = true,
//                        likeCount = 3,
//                        desc = "dummy, dummy, dummy",
//                        imageList = listOf("https://cdn-icons-png.flaticon.com/512/3135/3135715.png", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png")
//                    ),
//                    LookAroundData(
//                        key = "",
//                        profileImageUrl = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
//                        name = "David",
//                        postDate = listOf("2023", "01", "30"),
//                        isFollowing = true,
//                        like = false,
//                        likeCount = 3,
//                        desc = "dummy, dummy, dummy",
//                        imageList = listOf("https://cdn-icons-png.flaticon.com/512/3135/3135715.png", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png")
//                    )
//                )
//            )
        }
    }

    private fun intentPost(lookAround: LookAround) {
        // TODO: 해당 데이터의 상세 페이지로 이동하기
//        val intent = UserPostActivity.getIntent(requireActivity(), lookAroundData.key)
//        startActivity(intent)
    }

    private fun addToFollowingList(lookAround: LookAround) {
        // TODO: 해당 데이터를 팔로잉 목록에 추가하기
    }

    private fun addLike(lookAround: LookAround) {
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
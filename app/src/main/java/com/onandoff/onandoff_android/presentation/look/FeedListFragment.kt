package com.onandoff.onandoff_android.presentation.look

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.CategoryResponse
import com.onandoff.onandoff_android.data.model.FeedCategoryResponse
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.databinding.FragmentFeedListBinding
import com.onandoff.onandoff_android.presentation.look.viewmodel.FeedListViewModel
import gun0912.tedkeyboardobserver.TedKeyboardObserver
import kotlinx.coroutines.launch

class FeedListFragment : Fragment() {
    private var _binding: FragmentFeedListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<FeedListViewModel>(factoryProducer = {
        FeedListViewModel.Factory
    })

    private lateinit var feedListAdapter: FeedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedListBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
        setupViewModel()
        binding.srlFeedList.setOnRefreshListener {
            feedListAdapter.refresh()
            binding.srlFeedList.isRefreshing = false
        }
    }

    private fun setupView() {
        initLookAroundTabs()
        initFeedListRecyclerView(binding.rvFeedList)

        TedKeyboardObserver(requireActivity())
            .listen { isShow ->
                binding.spinner.isInvisible = isShow
                binding.srlFeedList.isInvisible = isShow

                if (!isShow) {
                    binding.editInputHashtag.clearFocus()
                }
            }
    }

    private fun setupListeners() {
        binding.editInputHashtag.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.editInputHashtag.hint = ""
            } else {
                binding.editInputHashtag.setHint(R.string.search_hashtag)
                binding.spinner.visibility = View.VISIBLE
                binding.rvFeedList.visibility = View.VISIBLE
            }
        }

        binding.editInputHashtag.setOnEditorActionListener { textView, actionId, keyEvent ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = textView?.text.toString() // # 없이 검색
                    viewModel.getSearchFeedResult(query)
                    Log.d("query", "FeedListFragment - setupListeners: query - $query")

                    closeKeyboard(binding.root)
                }
            }
            true
        }

        binding.editInputHashtag.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                binding.spinner.visibility = View.VISIBLE
                binding.rvFeedList.visibility = View.VISIBLE

                false
            }
            true
        }

        binding.layoutSpinnerFeedList.setOnClickListener {
            if (binding.srlFeedList.isInvisible) {
                closeKeyboard(binding.root)
            }
        }
    }

    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupViewModel() {
        with(viewModel) {
//            lifecycleScope.launch {
////                repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                    feedList
//                        .collect {
//                            Log.d("feedRequest", "$it")
//                            feedListAdapter.submitData(it)
//                        }
////                }
//            }
            feedList.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    it.collect {
                        Log.d("feedRequest", "$it")
                        feedListAdapter.submitData(it)
                    }
                }
            }

            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is FeedListViewModel.State.GetFeedListFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.GetFeedListFailed.Reason.BODY_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "body error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.GetFeedListFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "jwt error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.GetFeedListFailed.Reason.SERVER_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "server error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is FeedListViewModel.State.GetFeedFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.GetFeedFailed.Reason.BODY_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "body error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.GetFeedFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "jwt error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.GetFeedFailed.Reason.SERVER_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "server error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.GetFeedFailed.Reason.NO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "no profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is FeedListViewModel.State.SearchFeedFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.SearchFeedFailed.Reason.NO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "no profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {}
                            }
                        }
                        is FeedListViewModel.State.LikeOrNoLikeFeedFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.LikeOrNoLikeFeedFailed.Reason.UNAUTHORIZED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "unauthorized",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.LikeOrNoLikeFeedFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.LikeOrNoLikeFeedFailed.Reason.NO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "no profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.LikeOrNoLikeFeedFailed.Reason.INVALID_FEED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid feed error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is FeedListViewModel.State.FollowOrUnfollowFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.FollowOrUnfollowFailed.Reason.UNAUTHORIZED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "unauthorized",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.FollowOrUnfollowFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.FollowOrUnfollowFailed.Reason.INVALID_FROM_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid from profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.FollowOrUnfollowFailed.Reason.INVALID_TO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid to profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is FeedListViewModel.State.ReportFeedFailed -> {
                            when (state.reason) {
                                FeedListViewModel.State.ReportFeedFailed.Reason.PARAMETER_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "parameter error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.ReportFeedFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "jwt error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.ReportFeedFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                FeedListViewModel.State.ReportFeedFailed.Reason.INVALID_FEED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid feed error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        FeedListViewModel.State.Idle -> {}
                        is FeedListViewModel.State.GetFeedListSuccess -> {

                        }
                        is FeedListViewModel.State.GetFeedSuccess -> {

                        }
                        is FeedListViewModel.State.SearchFeedSuccess -> {

                        }
                        is FeedListViewModel.State.LikeOrNoLikeFeedSuccess -> {

                        }
                        is FeedListViewModel.State.FollowOrUnfollowSuccess -> {

                        }
                        is FeedListViewModel.State.ReportFeedSuccess -> {

                        }
                        is FeedListViewModel.State.GetCategoryListSuccess -> {
                            initSpinner(state.list)
                        }
                    }
                }
            }
        }
    }

    // 팔로잉 탭을 클릭할 경우 같은 API를 재호출 하되 fResult를 true로 해서 서버에 요청하셔야 합니다.
    // 이렇게 요청하시면 서버에서 팔로우 된 게시글만 필터링하여 클라이언트로 넘겨드립니다
    private fun initLookAroundTabs() {
        binding.tlLookAroundList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val type = ExploreType.values().getOrNull(tab.position) ?: ExploreType.Normal
                viewModel.changeExploreType(type)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initSpinner(list: List<CategoryResponse>) {
        val items = list.sortedBy { it.categoryId }

        val spinnerAdapter = object : ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_spinner
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                // 마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                val spinnerItemText = view.findViewById<View>(R.id.tv_item_spinner) as TextView
                spinnerItemText.text = ""
                // 아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                spinnerItemText.hint = binding.spinner.selectedItem.toString()

                return view
            }

            override fun getCount(): Int {
                // 마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter.addAll(items.map { it.categoryName }) // 아이템을 추가
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.setSelection(spinnerAdapter.count)
        binding.spinner.dropDownVerticalOffset = dipToPixels(55f).toInt()

        // 스피너 선택시 나오는 화면
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 아이템이 클릭되면 맨 위(position 0번)부터 순서대로 동작하게 된다.
                val value = parent!!.getItemAtPosition(position).toString()

                val category = items.getOrNull(position)
                if (category != null) {
                    getCategoryFeedList(category)
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

    private fun getCategoryFeedList(category: CategoryResponse) {
        viewModel.setSelectedCategory(category)
    }

    private fun initFeedListRecyclerView(recyclerView: RecyclerView) {
        feedListAdapter = FeedListAdapter(
            onProfileClick = ::intentPost,
            onFollowClick = ::onClickFollow,
            onLikeClick = ::onClickLike,
            onOptionClick = ::openOptionMenuBottomSheet
        )

        recyclerView.run {
            setHasFixedSize(true)
            adapter = feedListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun openOptionMenuBottomSheet(feedData: LookAroundFeedData) {
        val bottomSheetDialogFragment = BottomSheetFeedListOptionMenu.newInstance(feedId = feedData.feedId)
        bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun intentPost(feedData: LookAroundFeedData) {
        // TODO: 해당 데이터의 상세 페이지로 이동하기
//        val intent = PostingReadActivity.getIntent(requireActivity(), feedData.feedId)
//        startActivity(intent)
    }

    private fun onClickFollow(feedData: LookAroundFeedData) {
        viewModel.follow(feedData.profileId)
        feedData.isFollowing = !feedData.isFollowing
        feedListAdapter.notifyDataSetChanged()
    }

    private fun onClickLike(feedData: LookAroundFeedData) {
        viewModel.like(feedData.feedId)
        feedData.isLike = !feedData.isLike
        feedListAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = FeedListFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
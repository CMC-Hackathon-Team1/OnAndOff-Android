package com.onandoff.onandoff_android.presentation.look

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.CategoryResponse
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.databinding.FragmentLookAroundBinding
import com.onandoff.onandoff_android.presentation.home.otheruser.OtherUserFragment
import com.onandoff.onandoff_android.presentation.look.viewmodel.LookAroundViewModel
import gun0912.tedkeyboardobserver.TedKeyboardObserver
import kotlinx.coroutines.launch

class LookAroundFragment : Fragment() {
    private var _binding: FragmentLookAroundBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<LookAroundViewModel>(factoryProducer = {
        LookAroundViewModel.Factory
    })

    private lateinit var lookAroundFeedListAdapter: LookAroundFeedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLookAroundBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
        setupViewModel()
        binding.srlFeedList.setOnRefreshListener {
            if (viewModel.isQueryChanged()) {
                viewModel.refresh()
            } else {
                lookAroundFeedListAdapter.refresh()
            }
            binding.editInputHashtag.clearFocus()
        }
    }

    private fun setupView() {
        initLookAroundTabs()
//        binding.spinner.dropDownVerticalOffset = -20
        initFeedListRecyclerView(binding.rvFeedList)

        TedKeyboardObserver(requireActivity())
            .listen { isShow ->
                val isItemEmpty = lookAroundFeedListAdapter.itemCount == 0
                binding.spinner.isInvisible = isShow
                binding.rvFeedList.isInvisible = isShow || isItemEmpty
                binding.tvNothingFound.isVisible = !isShow && isItemEmpty

                if (!isShow) {
                    binding.editInputHashtag.clearFocus()
                }
            }

        lookAroundFeedListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0 && itemCount > 0) {
                    binding.rvFeedList.scrollTo(0, 0)
                }
            }
        })

        lookAroundFeedListAdapter.addLoadStateListener { loadStates: CombinedLoadStates ->
            val isEmptyVisible = (loadStates.source.refresh is LoadState.NotLoading
                && loadStates.append.endOfPaginationReached
                && lookAroundFeedListAdapter.itemCount < 1)

            val isLoadingVisible = loadStates.refresh is LoadState.Loading

            binding.rvFeedList.isVisible = !isLoadingVisible && !isEmptyVisible
//            binding.spinner.isVisible = !isLoadingVisible && !isEmptyVisible
            binding.tvNothingFound.isVisible = isEmptyVisible

            if (loadStates.append is LoadState.Loading
                || loadStates.append is LoadState.Error
                || isEmptyVisible
            ) {
                binding.srlFeedList.isRefreshing = false
            }
        }
    }

    private fun setupListeners() {
        binding.editInputHashtag.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.editInputHashtag.hint = ""
            } else {
                binding.editInputHashtag.setHint(R.string.search_hashtag)
            }
        }

        binding.editInputHashtag.setOnEditorActionListener { textView, actionId, keyEvent ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val query = textView?.text.toString() // # 없이 검색
                    binding.rvFeedList.isGone = true
                    viewModel.getSearchFeedResult(query)
                    Log.d("query", "FeedListFragment - setupListeners: query - $query")

                    closeKeyboard(binding.root)
                    binding.editInputHashtag.clearFocus()
                }
            }
            true
        }

        binding.layoutSpinnerFeedList.setOnClickListener {
            Log.d("binding.rvFeedList.isInvisible", "${binding.rvFeedList.isInvisible}")
            if (binding.rvFeedList.isInvisible) {
                closeKeyboard(binding.root)
            }

            if (binding.editInputHashtag.hasFocus()) {
                binding.editInputHashtag.clearFocus()
            }
        }

        binding.spinner.viewTreeObserver.addOnGlobalLayoutListener {
            binding.spinner.dropDownVerticalOffset = binding.spinner.dropDownVerticalOffset - 7
        }
    }

    private fun closeKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                state.collect { state ->
                    when (state) {
                        is LookAroundViewModel.State.GetFeedListFailed -> {
                            when (state.reason) {
                                LookAroundViewModel.State.GetFeedListFailed.Reason.BODY_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "body error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.GetFeedListFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "jwt error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.GetFeedListFailed.Reason.SERVER_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "server error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is LookAroundViewModel.State.SearchFeedFailed -> {
                            when (state.reason) {
                                LookAroundViewModel.State.SearchFeedFailed.Reason.NO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "no profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {}
                            }
                        }
                        is LookAroundViewModel.State.LikeOrNoLikeFeedFailed -> {
                            when (state.reason) {
                                LookAroundViewModel.State.LikeOrNoLikeFeedFailed.Reason.UNAUTHORIZED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "unauthorized",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.LikeOrNoLikeFeedFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.LikeOrNoLikeFeedFailed.Reason.NO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "no profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.LikeOrNoLikeFeedFailed.Reason.INVALID_FEED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid feed error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is LookAroundViewModel.State.FollowOrUnfollowFailed -> {
                            when (state.reason) {
                                LookAroundViewModel.State.FollowOrUnfollowFailed.Reason.UNAUTHORIZED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "unauthorized",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.FollowOrUnfollowFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.FollowOrUnfollowFailed.Reason.INVALID_FROM_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid from profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.FollowOrUnfollowFailed.Reason.INVALID_TO_PROFILE_ID -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid to profile id error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        is LookAroundViewModel.State.ReportFeedFailed -> {
                            when (state.reason) {
                                LookAroundViewModel.State.ReportFeedFailed.Reason.PARAMETER_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "parameter error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.ReportFeedFailed.Reason.JWT_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "jwt error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.ReportFeedFailed.Reason.DB_ERROR -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "db error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LookAroundViewModel.State.ReportFeedFailed.Reason.INVALID_FEED -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "invalid feed error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        LookAroundViewModel.State.Idle -> {}
                        is LookAroundViewModel.State.GetFeedListSuccess -> {

                        }
                        is LookAroundViewModel.State.SearchFeedSuccess -> {

                        }
                        is LookAroundViewModel.State.LikeOrNoLikeFeedSuccess -> {

                        }
                        is LookAroundViewModel.State.FollowOrUnfollowSuccess -> {

                        }
                        is LookAroundViewModel.State.ReportFeedSuccess -> {

                        }
                        is LookAroundViewModel.State.GetCategoryListSuccess -> {
                            initSpinner(state.list)
                        }
                    }
                }
            }
        }
    }

    // 팔로잉 탭을 클릭할 경우 같은 API 를 재호출 하되 fResult 를 true 로 해서 서버에 요청하셔야 합니다.
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

    private fun initSpinner(categoryList: List<CategoryResponse>) {
        val items = categoryList.sortedBy { it.categoryId }

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
                // 마지막 아이템은 힌트용으로만 사용하기 때문에 getCount 에 1을 빼줍니다.
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
                val itemValue = parent!!.getItemAtPosition(position).toString()

//                val newCategoryList = mutableListOf<CategoryResponse>()
//                val categoryValue = binding.spinner.selectedItem
//                if (!categoryValue.equals("카테고리 전체")) {
//                    for (i in items.indices) {
//                        if (items[i].categoryName?.equals(categoryValue) == true) {
//                            newCategoryList.add(items[4])
//                        } else {
//                            newCategoryList.add(items[i])
//                        }
//
//                        Log.d("onItemSelected", "onItemSelected: ${newCategoryList[i]}")
//                    }
//
//                    spinnerAdapter.clear()
//                    spinnerAdapter.addAll(newCategoryList.map { it.categoryName })
//
//                    val spinnerItemText = view?.findViewById<View>(R.id.tv_item_spinner) as TextView
//                    spinnerItemText.hint = categoryValue.toString()
//                }
//                else {
//                    val element = CategoryResponse(
//                        0,
//                        "카테고리 전체"
//                    )
//
//                    getCategoryFeedList(element, categoryList)
//                }
//                else {
//                    spinnerAdapter.clear()
//                    spinnerAdapter.addAll(items.map { it.categoryName })
//
//                    val spinnerItemText = view?.findViewById<View>(R.id.tv_item_spinner) as TextView
//                    spinnerItemText.hint = categoryValue.toString()
//                }

                val category = items.getOrNull(position)
                if (category != null) {
//                    val categoryIndex = items.indexOf(category)
//                    if (items[categoryIndex].categoryId == category.categoryId) {
//                        category.categoryName = "카테고리 전체"
//                    }
                    getCategoryFeedList(category, categoryList)
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

    var oldCategoryItemId: Int = 0
    var oldCategoryItemName: String = ""
    var oldCategoryItemIndex: Int = 0

    private fun getCategoryFeedList(
        category: CategoryResponse,
        categoryList: List<CategoryResponse>
    ) {
        viewModel.setSelectedCategory(category)

//        val oldCategoryItemIndex = categoryList.indexOf(category)
//        if (categoryList[oldCategoryItemIndex].oldCategoryItemId == category.categoryId) {
//            categoryList[oldCategoryItemIndex].oldCategoryItemName = category.categoryName
//        }
    }

    private fun initFeedListRecyclerView(recyclerView: RecyclerView) {
        lookAroundFeedListAdapter = LookAroundFeedListAdapter(
            onProfileClick = ::onFeedProfileClick,
            onFollowClick = ::onClickFollow,
            onLikeClick = ::onClickLike,
            onOptionClick = ::openOptionMenuBottomSheet
        )

        recyclerView.run {
            setHasFixedSize(true)
            adapter = lookAroundFeedListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    private fun openOptionMenuBottomSheet(feedData: LookAroundFeedData) {
        val bottomSheetDialogFragment =
            BottomSheetLookAroundFeedOptionMenu.newInstance(feedId = feedData.feedId)
        bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun onFeedProfileClick(feedData: LookAroundFeedData) {
        // 해당 데이터의 상세 페이지로 이동하기
        val intent = OtherUserFragment.newInstance(feedData.feedId, feedData.profileId)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.fcv_main, intent)?.commit()
        Log.d("onFeedProfileClick", "onFeedProfileClick: ${feedData.feedId} ${feedData.profileId}")
    }

    private fun onClickFollow(feedData: LookAroundFeedData) {
        viewModel.follow(feedData.profileId) {
            Log.d("isFollowing : ", "$it")

            feedData.isFollowing = it
            lookAroundFeedListAdapter.notifyDataSetChanged()
//            feedListAdapter.refresh()
        }
    }

    private fun onClickLike(feedData: LookAroundFeedData) {
        viewModel.like(feedData.feedId) {
            Log.d("isLike : ", "$it")
            feedData.isLike = it
            lookAroundFeedListAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = LookAroundFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
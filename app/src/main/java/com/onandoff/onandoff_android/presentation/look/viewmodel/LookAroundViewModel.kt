package com.onandoff.onandoff_android.presentation.look.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.CategoryResponse
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.FeedRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.presentation.look.ExploreType
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LookAroundViewModel(
    application: Application,
    private val feedRepository: FeedRepository
) : AndroidViewModel(application) {

    sealed class State {
        data class GetFeedListFailed(val reason: Reason) : State() {

            enum class Reason {
                BODY_ERROR,
                JWT_ERROR,
                SERVER_ERROR
            }
        }

        data class GetFeedListSuccess(val feedList: Flow<PagingData<LookAroundFeedData>>) : State()

        data class SearchFeedFailed(val reason: Reason) : State() {

            enum class Reason {
                NO_PROFILE_ID,
                NO_SEARCH_KEYWORD
            }
        }

        object SearchFeedSuccess : State()


        data class LikeOrNoLikeFeedFailed(val reason: Reason) : State() {

            enum class Reason {
                UNAUTHORIZED,
                DB_ERROR,
                NO_PROFILE_ID,
                INVALID_FEED
            }
        }

        data class LikeOrNoLikeFeedSuccess(val choice: Choice) : State() {
            enum class Choice {
                LIKE,
                NO_LIKE
            }
        }


        data class FollowOrUnfollowFailed(val reason: Reason) : State() {

            enum class Reason {
                UNAUTHORIZED,
                DB_ERROR,
                INVALID_FROM_PROFILE_ID,
                INVALID_TO_PROFILE_ID
            }
        }

        data class FollowOrUnfollowSuccess(val choice: Choice) : State() {
            enum class Choice {
                FOLLOW,
                UNFOLLOW
            }
        }


        data class ReportFeedFailed(val reason: Reason) : State() {

            enum class Reason {
                PARAMETER_ERROR,
                JWT_ERROR,
                DB_ERROR,
                INVALID_FEED
            }
        }

        data class GetCategoryListSuccess(val list: List<CategoryResponse>) : State()

        object ReportFeedSuccess : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _spinnerEntry = MutableStateFlow(
        emptyList<Int>()
    )

    val spinnerEntry: StateFlow<List<Int>?> = _spinnerEntry

    val feedId: MutableLiveData<Int> = MutableLiveData()

    val page: MutableLiveData<Int> = MutableLiveData()
    private var categoryId: Int = 0

    val fromProfileId: MutableLiveData<Int> = MutableLiveData()
    val toProfileId: MutableLiveData<Int> = MutableLiveData()

    val searchKeyword: MutableLiveData<String> = MutableLiveData()

    private val _emptyStateMessage =
        MutableLiveData(getApplication<Application>().getString(R.string.no_feed_found))
    val emptyStateMessage: LiveData<String>
        get() = _emptyStateMessage

    private val profileId: Int
        get() = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            -1
        )

    private val feedRequest = Channel<FeedRequest>()
    private val feedLoadEvent = feedRequest.receiveAsFlow()

    private val _feedList = MutableLiveData<Flow<PagingData<LookAroundFeedData>>>()
    val feedList: LiveData<Flow<PagingData<LookAroundFeedData>>> = _feedList

    private var latestQuery = ""

    private var currentExploreType: ExploreType = ExploreType.Normal
    private val fResult: Boolean
        get() = when (currentExploreType) {
            ExploreType.Normal -> false
            ExploreType.Following -> true
        }

    init {
        getFeedList(0) // category 필터링 없이 전체 피드를 받아오기

        getCategories()

        setupFeedLoadRequest()

        observeProfileChanged()
    }

    private fun setupFeedLoadRequest() {
        viewModelScope.launch {
            feedLoadEvent
                .collect {
                    latestQuery = it.query

                    val emptyMessageResId = if (!it.fResult && it.query.isEmpty()) {
                        R.string.no_feed_found
                    } else if (it.fResult && it.query.isEmpty()) {
                        R.string.no_following_found
                    } else {
                        R.string.nothing_found
                    }
                    _emptyStateMessage.value =
                        getApplication<Application>().getString(emptyMessageResId)

                    _feedList.value = feedRepository.getFeedListSource(it)
                }
        }
    }

    private fun observeProfileChanged() {
        viewModelScope.launch {
            SharePreference.prefs.observePreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID)
                .collect { profileId ->
                    Log.d("profileIdTest", "$profileId")
                    getFeedList(categoryId)
                }
        }
    }

    var oldCategoryItemId: Int = 0
    var oldCategoryItemName: String = ""
    var oldCategoryItemIndex: Int = 0

    private fun getCategories() {
        viewModelScope.launch {
            kotlin.runCatching {
                feedRepository.getCategories().result.orEmpty()
            }
                .map {
                    val element = CategoryResponse(
                        0,
                        getApplication<Application>().getString(R.string.category_all),
                    )
                    val result = listOf(element) + it // 맨 처음부터 카테고리 전체가 말풍선 목록에 있으면 안됨. 말 풍선 목록에는 4개만 있어야 함
//                    val result = it
                    result + element.copy(categoryId = result.size, isInvalid = true)
                }
                .onSuccess {
                    _state.value = State.GetCategoryListSuccess(it)
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun changeExploreType(type: ExploreType) {
        if (currentExploreType != type) {
            currentExploreType = type
            if (searchKeyword.value.isNullOrEmpty()) {
                viewModelScope.launch {
                    feedRequest.send(FeedRequest(profileId, categoryId, fResult))
                }
            }
        }
    }

    private fun getFeedList(categoryId: Int) {
        viewModelScope.launch {
            feedRequest.send(FeedRequest(profileId, categoryId, fResult))
        }
    }

    fun getSearchFeedResult(query: String) {
        viewModelScope.launch {
            feedRequest.send(FeedRequest(profileId, categoryId, fResult, query))
            Log.d("query", "FeedListViewModel - getSearchFeedResult: profileId - $profileId")
            Log.d("query", "FeedListViewModel - getSearchFeedResult: categoryId - $categoryId")
            Log.d("query", "FeedListViewModel - getSearchFeedResult: fResult - $fResult")
        }
    }

    fun like(feedId: Int, callback: (isLike: Boolean) -> Unit) {
        val request = LikeRequest(
            profileId = profileId,
            feedId = feedId
        )

        Log.d("request", "$request")

        viewModelScope.launch {
            kotlin.runCatching {
                feedRepository.addOrUndoLike(request)
            }
                .onSuccess {
                    if (it.statusCode == 2000 || it.statusCode == 2001) {
                        val isLike = it.message == "Like"
                        callback(isLike)
                    }
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.JwtError -> _state.value =
                                State.LikeOrNoLikeFeedFailed(State.LikeOrNoLikeFeedFailed.Reason.UNAUTHORIZED)
                            is NetworkError.DBError -> _state.value =
                                State.LikeOrNoLikeFeedFailed(State.LikeOrNoLikeFeedFailed.Reason.DB_ERROR)
                            is NetworkError.NoProfileIdError -> _state.value =
                                State.LikeOrNoLikeFeedFailed(State.LikeOrNoLikeFeedFailed.Reason.NO_PROFILE_ID)
                            is NetworkError.InvalidFeedDataError -> _state.value =
                                State.LikeOrNoLikeFeedFailed(State.LikeOrNoLikeFeedFailed.Reason.INVALID_FEED)
                            else -> {}
                        }
                    }
                }
        }
    }

    fun follow(followProfileId: Int, callback: (isFollowed: Boolean) -> Unit) {
        val request = FollowRequest(
            fromProfileId = profileId,
            toProfileId = followProfileId
        )

        viewModelScope.launch {
            kotlin.runCatching {
                feedRepository.addOrUndoFollow(request)
            }
                .onSuccess {
                    if (it.statusCode == 2101 || it.statusCode == 2102) {
                        val isFollowed = it.message == "Follow"
                        callback(isFollowed)
                    }
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.JwtError -> _state.value =
                                State.FollowOrUnfollowFailed(State.FollowOrUnfollowFailed.Reason.UNAUTHORIZED)
                            is NetworkError.DBError -> _state.value =
                                State.FollowOrUnfollowFailed(State.FollowOrUnfollowFailed.Reason.DB_ERROR)
                            is NetworkError.InvalidFromProfileId -> _state.value =
                                State.FollowOrUnfollowFailed(State.FollowOrUnfollowFailed.Reason.INVALID_FROM_PROFILE_ID)
                            is NetworkError.InvalidToProfileId -> _state.value =
                                State.FollowOrUnfollowFailed(State.FollowOrUnfollowFailed.Reason.INVALID_TO_PROFILE_ID)
                            else -> {}
                        }
                    }
                }
        }
    }

    fun setSelectedCategory(category: CategoryResponse) {
        Log.d("category : ", "$category")
        if (!category.isInvalid) {
            categoryId = category.categoryId
            getFeedList(category.categoryId)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setSpinnerEntry(entry: List<Int>) {
        viewModelScope.launch {
            _spinnerEntry.emit(entry)
        }
    }

    fun isQueryChanged(): Boolean {
        return latestQuery != searchKeyword.value.orEmpty()
    }

    fun refresh() {
        getFeedList(categoryId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return LookAroundViewModel(
                    application,
                    FeedRepositoryImpl(
                        FeedRemoteDataSourceImpl(
                            RetrofitClient.getClient()?.create(FeedInterface::class.java)!!
                        )
                    )
                ) as T
            }
        }
    }
}
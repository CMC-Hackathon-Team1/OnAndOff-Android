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
import com.onandoff.onandoff_android.data.model.FeedData
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

class FeedListViewModel(
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

        data class GetFeedListSuccess(val feedList: Flow<PagingData<FeedData>>) : State()

        data class GetFeedFailed(val reason: Reason) : State() {

            enum class Reason {
                BODY_ERROR,
                JWT_ERROR,
                SERVER_ERROR,
                NO_PROFILE_ID
            }
        }

        object GetFeedSuccess : State()


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

    private val profileId: Int
        get() = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            -1
        )

    private val feedRequest = Channel<FeedRequest>()
    private val feedLoadEvent = feedRequest.receiveAsFlow()

    private val _feedList = MutableLiveData<Flow<PagingData<FeedData>>>()
    val feedList: LiveData<Flow<PagingData<FeedData>>> = _feedList

    private var currentExploreType: ExploreType = ExploreType.Normal
    private val fResult: Boolean
        get() = when (currentExploreType) {
            ExploreType.Normal -> false
            ExploreType.Following -> true
        }

    init {
        getFeedList(0) // category 필터링 없이 전체 피드를 받아오기

        getCategories()

        viewModelScope.launch {
            feedLoadEvent
                .collect {
                    Log.d("FeedRequest", "$it")
                    _feedList.value = feedRepository.getFeedListSource(it)
                }
        }
    }

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
                    val result = listOf(element) + it
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

//
//        viewModelScope.launch {
//            kotlin.runCatching {
//                feedRepository.getFeedListSource(
//
//                )
//            }
//                .onSuccess {
//                    Log.d("LookAroundViewModel : ", "$it")
////                    _state.value = State.GetFeedListSuccess(it)
//                    _feedList.value = it
//                }
//                .onFailure {
//                    it.printStackTrace()
//                    if (it is NetworkError) {
//                        when (it) {
//                            is NetworkError.BodyError -> _state.value =
//                                State.GetFeedListFailed(State.GetFeedListFailed.Reason.BODY_ERROR)
//                            is NetworkError.JwtError -> _state.value =
//                                State.GetFeedListFailed(State.GetFeedListFailed.Reason.JWT_ERROR)
//                            is NetworkError.DBError -> _state.value =
//                                State.GetFeedListFailed(State.GetFeedListFailed.Reason.SERVER_ERROR)
//                            else -> {}
//                        }
//                    }
//                }
//        }
    }

    fun setSpinnerEntry(entry: List<Int>) {
        viewModelScope.launch {
            _spinnerEntry.emit(entry)
        }
    }

    private fun getFeedDetail(feedId: Int, profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                feedRepository.getFeedDetailResult(feedId, profileId)
            }
                .onSuccess {
                    Log.d("FeedListViewModel : ", "$it")
                    _state.value = State.GetFeedSuccess
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetFeedFailed(State.GetFeedFailed.Reason.BODY_ERROR)
                            is NetworkError.JwtError -> _state.value =
                                State.GetFeedFailed(State.GetFeedFailed.Reason.JWT_ERROR)
                            is NetworkError.DBError -> _state.value =
                                State.GetFeedFailed(State.GetFeedFailed.Reason.SERVER_ERROR)
                            is NetworkError.NoProfileIdError -> _state.value =
                                State.GetFeedFailed(State.GetFeedFailed.Reason.NO_PROFILE_ID)
                            else -> {}
                        }
                    }
                }
        }
    }

    fun getSearchFeedResult(query: String) {
        viewModelScope.launch {
            feedRequest.send(FeedRequest(profileId, categoryId, fResult, query))
            Log.d("query", "FeedListViewModel - getSearchFeedResult: profileId - $profileId")
            Log.d("query", "FeedListViewModel - getSearchFeedResult: categoryId - $categoryId")
            Log.d("query", "FeedListViewModel - getSearchFeedResult: fResult - $fResult")
        }
//        viewModelScope.launch {
//            kotlin.runCatching {
//                feedRepository.getFeedListSource(
//                    FeedRequest(profileId, categoryId, fResult, query)
//                )
//            }
//                .onSuccess {
//                    Log.d("LookAroundViewModel : ", "$it")
//                    _state.value = State.SearchFeedSuccess
//                }
//                .onFailure {
//                    if (it is NetworkError) {
//                        when (it) {
//                            is NetworkError.NoProfileIdError -> _state.value =
//                                State.SearchFeedFailed(State.SearchFeedFailed.Reason.NO_PROFILE_ID)
////                            is NetworkError.NoSearchKeyword -> _state.value =
////                                State.SearchFeedFailed(State.SearchFeedFailed.Reason.NO_SEARCH_KEYWORD)
//                            else -> {}
//                        }
//                    }
//                }
//        }
    }

    fun like(feedId: Int) {
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
                    Log.d("LookAroundViewModel : ", "$it")
//                    _state.value = State.LikeOrNoLikeFeedSuccess
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

    fun follow(followProfileId: Int) {
        val request = FollowRequest(
            fromProfileId = profileId,
            toProfileId = followProfileId
        )

        viewModelScope.launch {
            kotlin.runCatching {
                feedRepository.addOrUndoFollow(request)
            }
                .onSuccess {
                    Log.d("LookAroundViewModel : ", "$it")
//                    _state.value = State.FollowOrUnfollowSuccess
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


    fun reportFeed() {
        viewModelScope.launch {
            kotlin.runCatching {
//                feedRepository.reportFeed(feedId.value ?: 0)
            }
                .onSuccess {
                    Log.d("LookAroundViewModel : ", "$it")
                    _state.value = State.ReportFeedSuccess
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.PARAMETER_ERROR)
                            is NetworkError.JwtError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.JWT_ERROR)
                            is NetworkError.DBError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.DB_ERROR)
                            is NetworkError.InvalidFeedDataError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.INVALID_FEED)
                            else -> {}
                        }
                    }
                }
        }
    }

    fun setSelectedCategory(category: CategoryResponse) {
        Log.d("category", "$category")
        if (!category.isInvalid) {
            categoryId = category.categoryId
            getFeedList(category.categoryId)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return FeedListViewModel(
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
package com.onandoff.onandoff_android.presentation.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.user.MyPersonaInterface
import com.onandoff.onandoff_android.data.api.user.StatisticsInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.ProfileRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.remote.StatisticsRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.ProfileRepository
import com.onandoff.onandoff_android.data.repository.ProfileRepositoryImpl
import com.onandoff.onandoff_android.data.repository.StatisticsRepository
import com.onandoff.onandoff_android.data.repository.StatisticsRepositoryImpl
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val profileRepository: ProfileRepository,
    private val statisticsRepository: StatisticsRepository
) : AndroidViewModel(application) {

    sealed class State {
        data class GetPersonaFailed(val reason: Reason) : State() {
            enum class Reason {
                PARAMETER_ERROR,
                JWT_ERROR,
                DB_ERROR,
                NO_PROFILE
            }
        }

        data class GetPersonaListFailed(val reason: Reason) : State() {
            enum class Reason {
                PARAMETER_ERROR,
                JWT_ERROR,
                DB_ERROR,
                NO_PROFILE,
                NOT_MY_PROFILE
            }
        }

        data class GetMonthlyCountFailed(val reason: Reason) : State() {
            enum class Reason {
                NO_PROFILE_ID_OR_INVALID_VALUE,
                JWT_ERROR,
                SERVER_ERROR,
                JWT_TOKEN_AND_PROFILE_NOT_SAME
            }
        }

        data class GetPersonaSuccess(
            val myProfile: MyProfileItem
        ) : State()

        data class GetPersonaListSuccess(
            val profileList: List<MyProfileItem>
        ) : State()

        data class GetMonthlyCountSuccess(
            val monthlyLikesCount: Int,
            val monthMyFeedsCount: Int,
            val monthlyMyFollowersCount: Int
        ) : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    val personaName: MutableLiveData<String> = MutableLiveData() // 페르소나 이름
    val profileName: MutableLiveData<String> = MutableLiveData() // 사용자 이름

    val monthlyLikesCount: MutableLiveData<Int> = MutableLiveData() // 월 별 공감 수
    val monthlyMyFeedsCount: MutableLiveData<Int> = MutableLiveData() // 월 별 작성한 게시글 수
    val monthlyMyFollowersCount: MutableLiveData<Int> = MutableLiveData() // 월 별 팔로워 수

    var selectedProfile: MyProfileItem? = null
        private set

    private val profileList = mutableListOf<MyProfileItem>()


    // TODO: 홈 화면에 진입하면 페르소나 데이터와 공감(좋아요), 게시글, 팔로워 수가 보여야 함

    // TODO: 페르소나 목록 중에서 1개를 선택했을 때 해당 페르소나가 보이게 하기
    // 사용자 모든 프로필 받아오기 사용하시면 됩니다!
    // 프로필 별 세부 정보들은 ex) profileId 통해서 요청하면 될 것 같습니다!
    private fun getMyPersona(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { profileRepository.getMyProfile(profileId) }
                .onSuccess {
//                    _state.value = State.GetPersonaSuccess(it)
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetPersonaFailed(State.GetPersonaFailed.Reason.PARAMETER_ERROR)
                            is NetworkError.JwtError -> _state.value =
                                State.GetPersonaFailed(State.GetPersonaFailed.Reason.JWT_ERROR)
                            is NetworkError.DBError -> _state.value =
                                State.GetPersonaFailed(State.GetPersonaFailed.Reason.DB_ERROR)
                            is NetworkError.NoProfileError -> _state.value =
                                State.GetPersonaFailed(State.GetPersonaFailed.Reason.NO_PROFILE)
                            else -> {}
                        }
                    }
                }
        }
    }

    // TODO: 나의 페르소나 목록이 보이게 하기
    fun getMyPersonaList() {
        viewModelScope.launch {
            kotlin.runCatching { profileRepository.getMyProfileList() }
                .map {
                    it.result
                        ?.mapIndexed { index, myProfileResponse ->
                            MyProfileItem(myProfileResponse, index == 0)
                        }
                        .orEmpty()
                }
                .onSuccess {
                    profileList.clear()
                    profileList.addAll(it)
                    _state.value = State.GetPersonaListSuccess(it)
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetPersonaListFailed(State.GetPersonaListFailed.Reason.PARAMETER_ERROR)
                            is NetworkError.JwtError -> _state.value =
                                State.GetPersonaListFailed(State.GetPersonaListFailed.Reason.JWT_ERROR)
                            is NetworkError.DBError -> _state.value =
                                State.GetPersonaListFailed(State.GetPersonaListFailed.Reason.DB_ERROR)
                            is NetworkError.NoProfileError -> _state.value =
                                State.GetPersonaListFailed(State.GetPersonaListFailed.Reason.NO_PROFILE)
                            is NetworkError.NotMyProfileError -> _state.value =
                                State.GetPersonaListFailed(State.GetPersonaListFailed.Reason.NOT_MY_PROFILE)
                            else -> {}
                        }
                    }
                }
        }
    }


    fun setSelectedProfile(item: MyProfileItem) {
        selectedProfile = item
        val profileId = item.myProfile.profileId

        val newProfileList = profileList.map {
            it.copy(it.myProfile, isSelected = it.myProfile.profileId == item.myProfile.profileId)
        }
        profileList.clear()
        profileList.addAll(newProfileList)
        Log.d("newProfileList", "$newProfileList")
        _state.value = State.GetPersonaListSuccess(newProfileList)

        getMyPersona(profileId)
        getMonthlyStatistics(profileId) // like, my feed, follower
        Log.d("home",profileId.toString())
        // 프로필 변경시마다 현재 Profile id를 등록
        prefs.putSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            item.myProfile.profileId
        )

//        getMonthlyLikesCount(profileId)  // like 만
//        getMonthlyMyFeedsCount(profileId) // my feed
//        getMonthlyFollowersCount(profileId) // follower
    }


    // TODO: 페르소나 별 월 별 공감, 게시글, 팔로워 수가 보이게 하기
    private fun getMonthlyStatistics(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { statisticsRepository.getMonthlyStatistics(profileId) }
                .onSuccess {
                    _state.value = State.GetMonthlyCountSuccess(
                        it.result.monthlyLikesCount,
                        it.result.monthlyMyFeedsCount,
                        it.result.monthlyMyFollowersCount
                    )
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.NO_PROFILE_ID_OR_INVALID_VALUE)
                            is NetworkError.JwtError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_ERROR)
                            is NetworkError.ServerError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.SERVER_ERROR)
                            is NetworkError.JwtTokenAndProfileNotSame -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_TOKEN_AND_PROFILE_NOT_SAME)
                            else -> {}
                        }
                    }
                }
        }
    }


    // TODO: 페르소나 별 공감 수가 보이게 하기
    private fun getMonthlyLikesCount(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { statisticsRepository.getMonthlyLikesCount(profileId) }
                .onSuccess {
//                    _state.value = State.GetLikesCount(it.result.monthlyLikesCount)
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.NO_PROFILE_ID_OR_INVALID_VALUE)
                            is NetworkError.JwtError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_ERROR)
                            is NetworkError.ServerError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.SERVER_ERROR)
                            is NetworkError.JwtTokenAndProfileNotSame -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_TOKEN_AND_PROFILE_NOT_SAME)
                            else -> {}
                        }
                    }
                }
        }
    }

    // TODO: 페르소나 별 게시글 수가 보이게 하기
    private fun getMonthlyMyFeedsCount(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { statisticsRepository.getMonthlyMyFeedsCount(profileId) }
                .onSuccess {
                    _state.value = State.GetMonthlyCountSuccess(
                        monthlyLikesCount.value!!,
                        monthlyMyFeedsCount.value!!,
                        monthlyMyFollowersCount.value!!
                    )
                    //여기 이상함
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.NO_PROFILE_ID_OR_INVALID_VALUE)
                            is NetworkError.JwtError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_ERROR)
                            is NetworkError.ServerError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.SERVER_ERROR)
                            is NetworkError.JwtTokenAndProfileNotSame -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_TOKEN_AND_PROFILE_NOT_SAME)
                            else -> {}
                        }
                    }
                }
        }
    }

    // TODO: 페르소나 별 팔로워 수가 보이게 하기
    private fun getMonthlyFollowersCount(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { statisticsRepository.getMonthlyFollowersCount(profileId) }
                .onSuccess {
                    _state.value = State.GetMonthlyCountSuccess(
                        monthlyLikesCount.value!!,
                        monthlyMyFeedsCount.value!!,
                        monthlyMyFollowersCount.value!!
                    )
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.BodyError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.NO_PROFILE_ID_OR_INVALID_VALUE)
                            is NetworkError.JwtError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_ERROR)
                            is NetworkError.ServerError -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.SERVER_ERROR)
                            is NetworkError.JwtTokenAndProfileNotSame -> _state.value =
                                State.GetMonthlyCountFailed(State.GetMonthlyCountFailed.Reason.JWT_TOKEN_AND_PROFILE_NOT_SAME)
                            else -> {}
                        }
                    }
                }
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
                extras: CreationExtras,
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return HomeViewModel(
                    application,
                    ProfileRepositoryImpl(
                        ProfileRemoteDataSourceImpl(
                            RetrofitClient.getClient()?.create(MyPersonaInterface::class.java)!!
                        )
                    ),
                    StatisticsRepositoryImpl(
                        StatisticsRemoteDataSourceImpl(
                            RetrofitClient.getClient()
                                ?.create(StatisticsInterface::class.java)!!
                        )
                    )
                ) as T
            }
        }
    }
}

data class MyProfileItem constructor(
    val myProfile: MyProfileResponse,
    val isSelected: Boolean
)
package com.onandoff.onandoff_android.presentation.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.api.user.MyPersonaInterface
import com.onandoff.onandoff_android.data.api.user.StatisticsInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.CalendarDataSourceImpl
import com.onandoff.onandoff_android.data.remote.ProfileRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.remote.StatisticsRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.*
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val profileRepository: ProfileRepository,
    private val statisticsRepository: StatisticsRepository,
    private val calendarRepository: CalendarRepository
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

        data class GetCalendarFeedListSuccess(
            val calendarList : List<CalendarData>
        ) : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    val personaName: MutableLiveData<String> = MutableLiveData() // ???????????? ??????
    val profileName: MutableLiveData<String> = MutableLiveData() // ????????? ??????

    val monthlyLikesCount: MutableLiveData<Int> = MutableLiveData() // ??? ??? ?????? ???
    val monthlyMyFeedsCount: MutableLiveData<Int> = MutableLiveData() // ??? ??? ????????? ????????? ???
    val monthlyMyFollowersCount: MutableLiveData<Int> = MutableLiveData() // ??? ??? ????????? ???

    var selectedProfile: MyProfileItem? = null
        private set

    private val profileList = mutableListOf<MyProfileItem>()


    // TODO: ??? ????????? ???????????? ???????????? ???????????? ??????(?????????), ?????????, ????????? ?????? ????????? ???

    // TODO: ???????????? ?????? ????????? 1?????? ???????????? ??? ?????? ??????????????? ????????? ??????
    // ????????? ?????? ????????? ???????????? ??????????????? ?????????!
    // ????????? ??? ?????? ???????????? ex) profileId ????????? ???????????? ??? ??? ????????????!
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

    // TODO: ?????? ???????????? ????????? ????????? ??????
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
        getCalendarFeedList(profileId, 2023, "03")
        Log.d("home",profileId.toString())
        // ????????? ??????????????? ?????? Profile id??? ??????
        prefs.putSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            item.myProfile.profileId
        )

//        getMonthlyLikesCount(profileId)  // like ???
//        getMonthlyMyFeedsCount(profileId) // my feed
//        getMonthlyFollowersCount(profileId) // follower
    }

    private fun getCalendarFeedList(profileId: Int, year: Int, month : String) {
        viewModelScope.launch {
            kotlin.runCatching { calendarRepository.getCalendarFeedList(profileId, year, month) }
                .onSuccess {
                    _state.value = State.GetCalendarFeedListSuccess(it.result)
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

    // TODO: ???????????? ??? ??? ??? ??????, ?????????, ????????? ?????? ????????? ??????
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


    // TODO: ???????????? ??? ?????? ?????? ????????? ??????
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

    // TODO: ???????????? ??? ????????? ?????? ????????? ??????
    private fun getMonthlyMyFeedsCount(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { statisticsRepository.getMonthlyMyFeedsCount(profileId) }
                .onSuccess {
                    _state.value = State.GetMonthlyCountSuccess(
                        monthlyLikesCount.value!!,
                        monthlyMyFeedsCount.value!!,
                        monthlyMyFollowersCount.value!!
                    )
                    //?????? ?????????
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

    // TODO: ???????????? ??? ????????? ?????? ????????? ??????
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
                    ),
                    CalendarRepositoryImpl(
                        CalendarDataSourceImpl(
                            RetrofitClient.getClient()?.create(CalendarInterface::class.java)!!
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
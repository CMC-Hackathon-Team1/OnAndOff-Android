package com.onandoff.onandoff_android.presentation.home.setting.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.data.model.GetBlockedUserResponse
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.UserRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.UserRepository
import com.onandoff.onandoff_android.data.repository.UserRepositoryImpl
import com.onandoff.onandoff_android.data.request.BlockOrUnblockOtherUserRequest
import com.onandoff.onandoff_android.presentation.home.otheruser.BlockOtherUserViewModel
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlockedUserListViewModel(
    application: Application,
    private val userRepository: UserRepository
): AndroidViewModel(application) {

    sealed class State {
        data class GetBlockedUserListFailed(val reason: Reason) : State() {
            enum class Reason {
                DB_ERROR
            }
        }

        data class GetBlockedUserListSuccess(
            val blockedUserList: List<BlockedUser>
        ) : State()

        data class UnblockUserFailed(val reason: Reason) : State() {
            enum class Reason {
                DB_ERROR,
                INVALID_FROM_PROFILE_ID,
                INVALID_TO_PROFILE_ID
            }
        }

        object UnblockUserSuccess : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val profileId: Int
        get() = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            -1
        )

    init {
        getBlockedUserList(profileId)
    }

    fun getBlockedUserList(profileId: Int) {
        viewModelScope.launch {
            kotlin.runCatching { userRepository.getBlockedUserList(profileId) }
                .onSuccess {
//                    _state.value = State.GetBlockedUserListSuccess(it.result)
                }
                .onFailure {
                    _state.value =
                        State.GetBlockedUserListFailed(State.GetBlockedUserListFailed.Reason.DB_ERROR)
                }
        }
    }

    fun unblockUser(toProfileId: Int) {
        val request = BlockOrUnblockOtherUserRequest(
            fromProfileId = profileId,
            toProfileId = toProfileId,
            type = "UNBLOCK"
        )

        viewModelScope.launch {
            kotlin.runCatching { userRepository.blockOrUnblockOtherUser(request) }
                .onSuccess {
//                    if (it.statusCode == 3704) {
//                        val isBlocked = it.message == "UNBLOCK"
//                        callback(isBlocked)
//                    }
                    _state.value = State.UnblockUserSuccess
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
                            is NetworkError.ServerError -> _state.value =
                                State.UnblockUserFailed(State.UnblockUserFailed.Reason.DB_ERROR)
                            is NetworkError.InvalidFromProfileId -> _state.value =
                                State.UnblockUserFailed(State.UnblockUserFailed.Reason.INVALID_FROM_PROFILE_ID)
                            is NetworkError.InvalidToProfileId -> _state.value =
                                State.UnblockUserFailed(State.UnblockUserFailed.Reason.INVALID_TO_PROFILE_ID)
                            else -> {}
                        }
                    }
                }
        }
    }

    companion object {
        const val USER_ID = "user_id"

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return BlockedUserListViewModel(
                    application,
                    UserRepositoryImpl(
                        UserRemoteDataSourceImpl(
                            RetrofitClient.getClient()?.create(UserInterface::class.java)!!
                        )
                    )
                ) as T
            }
        }
    }
}
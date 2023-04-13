package com.onandoff.onandoff_android.presentation.home.otheruser

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.UserRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.UserRepository
import com.onandoff.onandoff_android.data.repository.UserRepositoryImpl
import com.onandoff.onandoff_android.data.request.BlockOrUnblockOtherUserRequest
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlockOtherUserViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {
    sealed class State {
        data class BlockOtherUserFailed(val reason: Reason) : State() {
            enum class Reason {
//                PARAMETER_ERROR,
//                JWT_ERROR,
                DB_ERROR,
                ALREADY_BLOCKED,
                INVALID_FROM_PROFILE_ID,
                INVALID_TO_PROFILE_ID,
            }
        }

        object BlockOtherUserSuccess : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val profileId: Int
        get() = SharePreference.prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,
            -1
        )

    fun blockOtherUser(toProfileId: Int) {
        val request = BlockOrUnblockOtherUserRequest(
            fromProfileId = profileId,
            toProfileId = toProfileId,
            type = "BLOCK"
        )

        viewModelScope.launch {
            kotlin.runCatching { userRepository.blockOrUnblockOtherUser(request) }
                .onSuccess {
//                    if (it.statusCode == 3703) {
//                        val isBlocked = it.message == "BLOCK"
//                        callback(isBlocked)
//                    }
                    _state.value = State.BlockOtherUserSuccess
                }
                .onFailure {
                    if (it is NetworkError) {
                        when (it) {
//                            is NetworkError.BodyError -> _state.value =
//                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.PARAMETER_ERROR)
//                            is NetworkError.JwtError -> _state.value =
//                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.JWT_ERROR)
                            is NetworkError.DBError -> _state.value =
                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.DB_ERROR)
                            is NetworkError.AlreadyReportedFeedError -> _state.value =
                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.ALREADY_BLOCKED)
                            is NetworkError.InvalidFromProfileId -> _state.value =
                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.INVALID_FROM_PROFILE_ID)
                            is NetworkError.InvalidToProfileId -> _state.value =
                                State.BlockOtherUserFailed(State.BlockOtherUserFailed.Reason.INVALID_TO_PROFILE_ID)
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

                return BlockOtherUserViewModel(
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
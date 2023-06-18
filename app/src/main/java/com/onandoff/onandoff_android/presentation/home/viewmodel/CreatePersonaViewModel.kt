package com.onandoff.onandoff_android.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.user.MyPersonaInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.ProfileRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.ProfileRepository
import com.onandoff.onandoff_android.data.repository.ProfileRepositoryImpl
import com.onandoff.onandoff_android.data.request.CreateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class CreatePersonaViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    sealed class State {
        data class CreateFailed(val reason: Reason) : State() {

            enum class Reason {
                EMPTY_PERSONA_NAME,
                EMPTY_PROFILE_NAME,
                BODY_ERROR,
                JWT_ERROR,
                SERVER_ERROR,
                LIMIT_PROFILE_COUNT,
                ALREADY_EXIST_PROFILE_ERROR
            }
        }

        object Success : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    val personaName: MutableLiveData<String> = MutableLiveData()
    val profileName: MutableLiveData<String> = MutableLiveData()
    val introduce: MutableLiveData<String> = MutableLiveData()

    private val _image: MutableLiveData<String> = MutableLiveData()
    val image: LiveData<String>
        get() = _image

    fun setPersonaImagePath(filePath: String) {
        Log.d("setPersonaImagePath", "setPersonaImagePath: $filePath")
        _image.value = filePath
    }

    fun onCreatePersona() {
        val personaName = personaName.value.orEmpty()
        val profileName = profileName.value.orEmpty()
        val statusMessage = introduce.value.orEmpty()
        val imagePath = image.value.orEmpty()

        when {
            personaName.isEmpty() -> {
                _state.value = State.CreateFailed(State.CreateFailed.Reason.EMPTY_PERSONA_NAME)
            }
            profileName.isEmpty() -> {
                _state.value = State.CreateFailed(State.CreateFailed.Reason.EMPTY_PROFILE_NAME)
            }
            else -> {
                val request = CreateProfileRequest(
                    profileName = profileName,
                    personaName = personaName,
                    statusMessage = statusMessage,
                    imagePath = File(imagePath)
                )

                viewModelScope.launch {
                    kotlin.runCatching {
                        profileRepository.createProfile(request)
                    }
                        .onSuccess {
                            Log.d("CreatePersonaViewModel", "$it")
                            _state.value = State.Success
                        }
                        .onFailure {
                            if (it is NetworkError) {
                                when (it) {
                                    is NetworkError.AlreadyExistProfileError -> {
                                        _state.value = State.CreateFailed(State.CreateFailed.Reason.ALREADY_EXIST_PROFILE_ERROR)
                                    }
                                    is NetworkError.BodyError -> {
                                        _state.value = State.CreateFailed(State.CreateFailed.Reason.BODY_ERROR)
                                    }
                                    is NetworkError.JwtError -> {
                                        _state.value = State.CreateFailed(State.CreateFailed.Reason.JWT_ERROR)
                                    }
                                    is NetworkError.LimitProfileCountError -> {
                                        _state.value = State.CreateFailed(State.CreateFailed.Reason.LIMIT_PROFILE_COUNT)
                                    }
                                    is NetworkError.ServerError -> {
                                        _state.value = State.CreateFailed(State.CreateFailed.Reason.SERVER_ERROR)
                                    }
                                    else -> {
                                        it.printStackTrace()
                                    }
                                }
                            }
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
                extras: CreationExtras
            ): T {
                return CreatePersonaViewModel(
                    ProfileRepositoryImpl(
                        ProfileRemoteDataSourceImpl(
                            RetrofitClient.getClient()?.create(MyPersonaInterface::class.java)!!
                        )
                    ),
                ) as T
            }
        }
    }
}
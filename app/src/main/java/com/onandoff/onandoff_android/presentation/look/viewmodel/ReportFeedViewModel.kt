package com.onandoff.onandoff_android.presentation.look.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.error.NetworkError
import com.onandoff.onandoff_android.data.remote.FeedRemoteDataSourceImpl
import com.onandoff.onandoff_android.data.repository.FeedRepository
import com.onandoff.onandoff_android.data.repository.FeedRepositoryImpl
import com.onandoff.onandoff_android.data.request.ReportFeedRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportFeedViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : AndroidViewModel(application) {
    sealed class State {
        data class ReportFeedFailed(val reason: Reason) : State() {
            enum class Reason {
                PARAMETER_ERROR,
                JWT_ERROR,
                DB_ERROR,
                NO_FEED,
                ALREADY_REPORTED,
                INVALID_REPORT_CATEGORY_ID,
                NO_REPORT_ETC_REASON
            }
        }

        object ReportFeedSuccess : State()

        object Idle : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val feedId: Int = requireNotNull(savedStateHandle[FEED_ID])

    fun reportFeed(reportedCategoryId: Int, content: String?) {
        val request = ReportFeedRequest(
            feedId = feedId,
            reportedCategoryId = reportedCategoryId,
            content = content
        )

        viewModelScope.launch {
            kotlin.runCatching { feedRepository.reportFeed(request) }
                .onSuccess {
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
                            is NetworkError.NoProfileError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.NO_FEED)
                            is NetworkError.AlreadyReportedFeedError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.ALREADY_REPORTED)
                            is NetworkError.InvalidReportCategoryIdError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.INVALID_REPORT_CATEGORY_ID)
                            is NetworkError.NoReportReasonError -> _state.value =
                                State.ReportFeedFailed(State.ReportFeedFailed.Reason.NO_REPORT_ETC_REASON)
                            else -> {}
                        }
                    }
                }
        }
    }

    companion object {
        const val FEED_ID = "feed_id"

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

                return ReportFeedViewModel(
                    application,
                    savedStateHandle,
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
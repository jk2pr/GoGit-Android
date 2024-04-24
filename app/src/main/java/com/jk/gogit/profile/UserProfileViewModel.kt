package com.jk.gogit.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.feed.services.FeedExecutor
import com.jk.gogit.overview.model.OverViewTabData
import com.jk.gogit.profile.services.UserProfileExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModel(
    private val userProfileExecutor: UserProfileExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
) : ViewModel() {

    private val _feedStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val feedStateFlow = _feedStateFlow.asStateFlow()
    lateinit var overViewScreenData: OverViewTabData.OverViewScreenData

    init {
        setState(mainState = MainState.FetchEvent)
    }

    @ExperimentalCoroutinesApi
    fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FetchEvent -> {
                    flow {
                        emit(UiState.Loading)
                        val overViewTabData = userProfileExecutor.execute(login = login)
                        overViewScreenData = overViewTabData.toOverViewScreenData()
                        emit(UiState.Content(overViewScreenData))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _feedStateFlow.value = it
                    }
                }

                is MainState.RefreshEvent -> {
                }

                is MainState.FollowEvent -> {
                    flow {
                        emit(UiState.Loading)
                        userProfileExecutor.followUser(userId = mainState.id)
                        val temp = overViewScreenData.copy(
                            user = overViewScreenData.user?.copy(viewerIsFollowing = true)
                        )
                        emit(UiState.Content(temp))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _feedStateFlow.value = it
                    }

                }
                is MainState.UnFollowEvent -> {
                    flow {
                        emit(UiState.Loading)
                        userProfileExecutor.unFollowUser(userId = mainState.id)
                        val temp = overViewScreenData.copy(
                            user = overViewScreenData.user?.copy(viewerIsFollowing = false)
                        )
                        emit(UiState.Content(temp))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _feedStateFlow.value = it
                    }

                }
            }
        }


    sealed class MainState {
        object FetchEvent : MainState()
        object RefreshEvent : MainState()
        data class FollowEvent(val id: String) : MainState()
        data class UnFollowEvent(val id: String) : MainState()
    }
}


package com.jk.gogit.users.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.feed.services.FeedExecutor
import com.jk.gogit.users.services.UserListExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserListViewModel(
    private val userListExecutor: UserListExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String
) : ViewModel() {

    private val _userListStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val userListStateFlow = _userListStateFlow.asStateFlow()

    init {
        setState(MainState.FeedEvent)
    }

    private fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FeedEvent -> {
                    flow {
                        emit(UiState.Loading)
                        val params = login.split("/")
                        val user = params.first()
                        var requestType = ""
                        var repo = ""
                        if (params.size == 3)
                            repo = params[1]
                        requestType = params.last()


                        val result = if (requestType == "stargazers")
                            userListExecutor.executeStargazers(
                                page = 1,
                                perPage = 10,
                                user = user,
                                repoName = repo,
                            )
                        else
                            userListExecutor.execute(
                                page = 1,
                                perPage = 10,
                                user = user,
                                type = requestType
                            )
                        emit(UiState.Content(result))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _userListStateFlow.value = it
                    }
                }

                is MainState.RefreshEvent -> {
                }
            }
        }

    sealed class MainState {
        object FeedEvent : MainState()
        object RefreshEvent : MainState()
    }
}


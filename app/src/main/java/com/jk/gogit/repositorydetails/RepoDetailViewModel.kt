package com.jk.gogit.repositorydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.extensions.printifyMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RepoDetailViewModel(
    private val repoDetailExecutor: RepoDetailExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
    private val repo: String,
    private val path: String
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
                        val result = repoDetailExecutor.execute(
                            page = 1,
                            perPage = 10,
                            user = login,
                            repo = repo,
                            path = path
                        )

                        emit(UiState.Content(result))
                    }.catch {
                        emit(UiState.Error(it.printifyMessage()))
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




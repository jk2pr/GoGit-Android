package com.jk.gogit.repositorydetails.commits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class CommitListViewModel(
    private val commitListExecutor: CommitListExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
    private val repo: String,
    private val path: String
) : ViewModel() {

    private val _commitListStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val commListStateFlow = _commitListStateFlow.asStateFlow()

    init {
        setState(MainState.FeedEvent)
    }

    private fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FeedEvent -> {
                    flow {
                        emit(UiState.Loading)
                        val result = commitListExecutor.execute(
                            page = 1,
                            perPage = 10,
                            user = login,
                            repo = repo,
                            branch = path
                        )

                        emit(UiState.Content(result))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _commitListStateFlow.value = it
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

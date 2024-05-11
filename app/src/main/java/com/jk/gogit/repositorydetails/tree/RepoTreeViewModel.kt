package com.jk.gogit.repositorydetails.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoppers.GetRepositoryTreeQuery
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class RepoTreeViewModel(
    private val repoTreeExecutor: RepoTreeExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
    private val repo: String,
    private var path: String
) : ViewModel() {

    private val _userListStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val userListStateFlow = _userListStateFlow.asStateFlow()

    init {
        setState(MainState.FeedEvent)
    }

    fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FeedEvent -> {
                    flow {
                        //  emit(UiState.Loading)
                        val result = repoTreeExecutor.execute(
                            user = login,
                            repo = repo,
                            path = path
                        )

                        val pathToFile = toPathToFile(
                            path = if (path.last() == ':') "" else path, //if this is from branch, then path is empty
                            file = result,
                        )
                        emit(UiState.Content(pathToFile))
                    }.catch {
                        // emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _userListStateFlow.value = it
                    }
                }

                is MainState.RefreshEvent -> {
                    flow {
                        val newPath = mainState.newPath
                        emit(UiState.Loading)
                        val finalPath = StringBuffer(path)
                        if (finalPath.last() == ':')
                        //this is branch
                            finalPath.append(newPath)
                        else
                            finalPath.append("/$newPath")
                        path = finalPath.toString()
                        val result =
                            repoTreeExecutor.execute(user = login, repo = repo, path = path)
                        val pathToFile = toPathToFile(path = newPath, file = result)
                        emit(UiState.Content(pathToFile))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _userListStateFlow.value = it
                    }

                }
            }
        }

    private fun toPathToFile(
        path: String,
        file: List<GetRepositoryTreeQuery.Entry>,
    ) = PathToFile(
        path = path,
        file = file,
    )


    sealed class MainState {
        data object FeedEvent : MainState()
        class RefreshEvent(val newPath: String) : MainState()
    }


}

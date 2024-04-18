package com.jk.gogit.repos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoppers.fragment.Repos
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.repos.services.RepoExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RepoListViewModel(
    private val repoExecutor: RepoExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
) : ViewModel() {

    private val _repoStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val repoStateFlow = _repoStateFlow.asStateFlow()
    private val _languageMapStateFlow = MutableStateFlow<Map<String, List<Repos?>>>(emptyMap())
    val languageMapStateFlow = _languageMapStateFlow.asStateFlow()

    private val result = mutableListOf<Repos?>()

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
                        val r = repoExecutor.execute(user = login)
                        result.clear()
                        result.addAll(r)
                        emit(UiState.Content(result))
                        updateLanguageMap(result)
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _repoStateFlow.value = it
                    }
                }

                is MainState.FilterEvent -> {
                    val language = mainState.language
                    flow {
                        emit(UiState.Loading)
                        if (language.isNullOrEmpty()) {
                            emit(UiState.Content(result))
                            return@flow
                        }

                        val filtered =
                            result.filter { item ->
                                (item as Repos).primaryLanguage?.name.orEmpty() == language
                            }
                        emit(UiState.Content(filtered))
                    }.flowOn(dispatchers.main).collect {
                        _repoStateFlow.value = it
                    }

                }
            }
        }

    sealed class MainState {
        data object FetchEvent : MainState()
        data class FilterEvent(val language: String?) : MainState()
    }
    private fun updateLanguageMap(nodes: List<Repos?>) {
        val languageMap = nodes.groupBy { me ->
            (me as Repos).primaryLanguage?.name.orEmpty()
        }.filterKeys { it.isNotEmpty() }
        _languageMapStateFlow.value = languageMap
        Log.d("RepoViewModel", "languageMap: $languageMap")
    }
}


package com.jk.gogit.search// ViewModel file: RepoSearchViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoppers.type.SearchType
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchExecutor: SearchExecutor,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private var _searchQuery = ""
    private val _searchStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val searchStateFlow = _searchStateFlow.asStateFlow()
    private var _typeStateFlow = SearchType.REPOSITORY

    fun performSearch(query: String = _searchQuery, type: SearchType = _typeStateFlow) {
        _searchQuery = query
        viewModelScope.launch {
            flow {
                if (query.length <= 5) {
                    _typeStateFlow = SearchType.REPOSITORY
                    emit(UiState.Content(emptyList<Any>()))
                    return@flow
                }
                emit(UiState.Loading)
                val repositories =
                    searchExecutor.execute(user = _searchQuery, type = type)
                emit(UiState.Content(repositories))
                // updateLanguageMap(repositories)
            }.catch { e ->
                emit(UiState.Error(e.message ?: "An error occurred"))
            }.flowOn(dispatchers.main).collect {
                _searchStateFlow.value = it
            }
        }
    }

    fun updateType(type: SearchType) {
        _typeStateFlow = type
        performSearch()
    }
}

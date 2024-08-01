package com.jk.gogit.organisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.extensions.printifyMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OrgListViewModel(
    private val orgExecutor: OrgExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
) : ViewModel() {

    private val _orgStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val orgStateFlow = _orgStateFlow.asStateFlow()


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
                        val r = orgExecutor.execute(user = login,)
                        emit(UiState.Content(r))
                    }.catch {
                        emit(UiState.Error(it.printifyMessage()))
                    }.flowOn(dispatchers.main).collect {
                        _orgStateFlow.value = it
                    }
                }


            }
        }

    sealed class MainState {
        data object FetchEvent : MainState()
    }
}


package com.jk.gogit.orgdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OrgDetailsViewModel(
    private val orgDetailsExecutor: OrgDetailsExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
) : ViewModel() {

    private val _feedStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val feedStateFlow = _feedStateFlow.asStateFlow()

    init {
        setState(mainState = MainState.FetchEvent)
    }

    @ExperimentalCoroutinesApi
    private fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FetchEvent -> {
                    flow {
                        emit(UiState.Loading)
                        val overViewTabData = orgDetailsExecutor.execute(login = login )
                        emit(UiState.Content(overViewTabData.toOverViewScreenData()))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _feedStateFlow.value = it
                    }
                }

                is MainState.RefreshEvent -> {
                }
            }
        }

    sealed class MainState {
        object FetchEvent : MainState()
        object RefreshEvent : MainState()
    }
}


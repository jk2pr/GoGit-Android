package com.jk.gogit.home

import android.util.Log
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
class FeedViewModel(
    private val feedExecutor: FeedExecutor,
    private val dispatchers: DispatcherProvider,
    private val login:String
) : ViewModel() {

    private val _feedStateFlow = MutableStateFlow<UiState>(UiState.Empty)
     val feedStateFlow = _feedStateFlow.asStateFlow()

    init {
        setState(MainState.FeedEvent)
    }
    @ExperimentalCoroutinesApi
     private fun setState(mainState: MainState) =
        viewModelScope.launch {
            when (mainState) {
                is MainState.FeedEvent -> {
                    flow {
                        emit(UiState.Loading)
                        Log.d("FeedViewModel", "result:Loading")
                        val result = feedExecutor.execute(page = 1, perPage = 100)
                        Log.d("FeedViewModel", "result: $result")
                        emit(UiState.Content(result))
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
        object FeedEvent : MainState()
        object RefreshEvent : MainState()
    }
}

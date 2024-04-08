package com.jk.gogit.feed.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.feed.services.FeedExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
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
                        val result = feedExecutor.execute(page = 1, perPage = 100, user = login)
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


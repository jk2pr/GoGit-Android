/*
package com.jk.gogit.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jk.gogit.DispatcherProvider
import com.jk.gogit.UiState
import com.jk.gogit.feed.services.FeedExecutor
import com.jk.gogit.overview.services.OverViewExecutor
import com.jk.gogit.profile.services.UserProfileExecutor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OverViewModel(
    private val overViewExecutor: OverViewExecutor,
    private val dispatchers: DispatcherProvider,
    private val login: String,
) : ViewModel() {

    private val _overviewStateFlow = MutableStateFlow<UiState>(UiState.Empty)
    val overviewStateFlow = _overviewStateFlow.asStateFlow()

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
                        val result = overViewExecutor.execute(user = login)
                        emit(UiState.Content(result))
                    }.catch {
                        emit(UiState.Error(it.message.toString()))
                    }.flowOn(dispatchers.main).collect {
                        _overviewStateFlow.value = it
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

*/

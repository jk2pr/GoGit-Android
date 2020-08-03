package com.jk.gogit.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jk.gogit.model.login.AccessToken
import com.jk.gogit.ui.main.login.data.response.Resource
import com.jk.gogit.ui.main.login.services.LoginExecutor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception


class LoginViewModel @ViewModelInject constructor(
        private val loginExecutor: LoginExecutor,
        @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _finalMutableLiveData = MutableLiveData<Resource<AccessToken>>()

    val finalDataLiveData: LiveData<Resource<AccessToken>>
        get() = _finalMutableLiveData

    @ExperimentalCoroutinesApi
    fun setState(mainState: MainState) {
        viewModelScope.launch {
            when (mainState) {
                is MainState.LoginEvent -> {
                    loginExecutor.execute()
                            .onEach {
                                _finalMutableLiveData.value = it
                            }.catch { e ->
                                print("Error $e")
                            }.collect{

                            }
                }
            }
        }
    }

    sealed class MainState {

        object LoginEvent : MainState()

        object None : MainState()
    }


}
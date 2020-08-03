package com.jk.gogit.ui.main.login.services

import com.jk.gogit.exception.UserUnAuthorizedException
import com.jk.gogit.model.login.AccessToken
import com.jk.gogit.model.login.AuthRequestModel
import com.jk.gogit.network.api.ILogin
import com.jk.gogit.ui.main.login.data.response.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.handleCoroutineException
import javax.inject.Inject

class LoginExecutor
@Inject constructor(private val iApi: ILogin) {

    /**
     * Show loading
     * Get Data from network
     * Show FinalData
     */

    val handler=CoroutineExceptionHandler{ _, throwable ->


    }

    suspend fun execute(): Flow<Resource<AccessToken>> = flow {
        emit(Resource.Loading)

        try {
            val loginData = iApi.authorizations(AuthRequestModel().generate())
            emit(Resource.Success(loginData))
        } catch (e: UserUnAuthorizedException) {
            emit(Resource.Error(e))
        }


    }

}
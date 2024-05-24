package com.jk.gogit.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.internal.zzab
import com.hoppers.networkmodule.network.AuthManager
import com.jk.gogit.MainActivity
import com.jk.gogit.profile.model.RawUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed class AuthenticationState {
    data object Initial : AuthenticationState()
    data object Authenticated : AuthenticationState()
    data object AuthenticationFailed : AuthenticationState()
    data object Loading : AuthenticationState()
}

class AuthViewModel : ViewModel() {
    private val _authenticationState =
        MutableStateFlow<AuthenticationState>(AuthenticationState.Initial)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState

    fun signInWithGithub(provider: OAuthProvider.Builder, activity: MainActivity) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && !AuthManager.getAccessToken().isNullOrEmpty()) {
            _authenticationState.value = AuthenticationState.Authenticated
            return
        }

        _authenticationState.value = AuthenticationState.Loading // Set loading state

        val pendingResultTask = firebaseAuth.pendingAuthResult
        pendingResultTask?.addOnSuccessListener {
            _authenticationState.value = AuthenticationState.Authenticated
        }?.addOnFailureListener {
            // Handle failure
            _authenticationState.value = AuthenticationState.AuthenticationFailed
        } ?: firebaseAuth
            .startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                val profileJson = (authResult.user?.providerData?.last() as zzab).zzb()
                val json = Json.parseToJsonElement(profileJson.toString()).jsonObject
                val rawUserInfoString = json["rawUserInfo"]?.jsonPrimitive?.content ?: ""
                val rawUserInfoJson = Json.parseToJsonElement(rawUserInfoString).jsonObject
                val rawUserInfo: RawUserInfo = Json.decodeFromString(rawUserInfoJson.toString())
                val login = rawUserInfo.login
                val accessToken = (authResult.credential as OAuthCredential).accessToken
                AuthManager.saveAccessToken(accessToken.toString(), login)
                _authenticationState.value = AuthenticationState.Authenticated
            }
            .addOnFailureListener {
                // Handle failure
                _authenticationState.value = AuthenticationState.AuthenticationFailed
            }
    }
}



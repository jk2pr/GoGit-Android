package com.jk.gogit.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.hoppers.networkmodule.AuthManager
import com.jk.gogit.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
                val profileData = authResult.additionalUserInfo?.profile
                if (profileData != null) {
                    val login = profileData["login"] as String
                    val avatarUrl = profileData["avatar_url"] as String

                    val accessToken = (authResult.credential as? OAuthCredential)?.accessToken
                    if (accessToken != null)

                        AuthManager.saveAccessToken(
                            token = accessToken.toString(),
                            login = login,
                            avatarUrl = avatarUrl,
                        )
                    _authenticationState.value = AuthenticationState.Authenticated
                }
            }
            .addOnFailureListener {
                // Handle failure
                _authenticationState.value = AuthenticationState.AuthenticationFailed
            }
    }
}



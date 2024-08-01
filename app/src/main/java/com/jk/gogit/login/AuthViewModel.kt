package com.jk.gogit.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.hoppers.networkmodule.AuthManager
import com.hoppers.networkmodule.model.UserProfile
import com.jk.gogit.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class AuthenticationState {
    data object Initial : AuthenticationState()
    data class Authenticated(val profile: Any?) : AuthenticationState()
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
        if (currentUser != null && !AuthManager.getAccessToken()
                .isNullOrEmpty() && AuthManager.getAvatarUrl() != null
        ) {
            _authenticationState.value =
                AuthenticationState.Authenticated(profile = currentUser.providerData.first())
            return
        }

        _authenticationState.value = AuthenticationState.Loading // Set loading state

        val pendingResultTask = firebaseAuth.pendingAuthResult
        pendingResultTask?.addOnSuccessListener {
            _authenticationState.value =
                AuthenticationState.Authenticated(it.user?.providerData?.first())
        }?.addOnFailureListener {
            // Handle failure
            _authenticationState.value = AuthenticationState.AuthenticationFailed
        } ?: firebaseAuth
            .startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                val profileData = authResult.additionalUserInfo?.profile
                val mappedProfileData = profileData?.mapValues { it.value.toString() }

                val jsonString = Json.encodeToString(mappedProfileData)
                val userProfile: UserProfile = Json.decodeFromString(jsonString)

                _authenticationState.value =
                    AuthenticationState.Authenticated(profile = userProfile)

                val accessToken = (authResult.credential as? OAuthCredential)?.accessToken
                if (!accessToken.isNullOrBlank())
                    AuthManager.saveAccessToken(
                        token = accessToken,
                        avatarUrl = userProfile.avatarUrl.orEmpty(),
                        login = userProfile.login.orEmpty()
                    )
                //  AuthManager.saveUserData(data= jsonString)
            }

            .addOnFailureListener {
                // Handle failure
                _authenticationState.value = AuthenticationState.AuthenticationFailed
            }
    }
}



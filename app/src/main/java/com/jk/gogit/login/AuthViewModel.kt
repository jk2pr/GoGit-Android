package com.jk.gogit.login

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.hoppers.networkmodule.AuthManager
import com.hoppers.networkmodule.model.UserProfile
import com.jk.gogit.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

sealed class AuthenticationState {
    data object Initial : AuthenticationState()
    data class Authenticated(val profile: Any?) : AuthenticationState()
    data class AuthenticationFailed(val message: String?) : AuthenticationState()
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
        }?.addOnFailureListener { exception ->
            _authenticationState.value = AuthenticationState.AuthenticationFailed(exception.message)
        } ?: run {

            val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com".toUri())
            if (activity.packageManager.resolveActivity(browserIntent, 0) == null) {
                _authenticationState.value = AuthenticationState.AuthenticationFailed(
                    "No web browser is installed. Please install one to proceed with authentication."
                )
                return
            }
            firebaseAuth
                .startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->
                    val profileData = authResult.additionalUserInfo?.profile
                    val mappedProfileData = profileData?.mapValues { it.value.toString() }

                    val j = Json { ignoreUnknownKeys = true }
                    val jsonString = j.encodeToString(mappedProfileData)
                    val userProfile: UserProfile = j.decodeFromString(jsonString)

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
                    _authenticationState.value =
                        AuthenticationState.AuthenticationFailed("Authentication failed")
                }
        }
    }

    fun resetState() {
        _authenticationState.value = AuthenticationState.Initial
    }
}

package com.jk.gogit.login.screen

import com.hoppers.networkmodule.network.AuthManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.internal.zzab
import com.jk.gogit.MainActivity
import com.jk.gogit.R
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.login.AuthRequestModel
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.profile.model.RawUserInfo
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


@Composable
fun LoginScreen() {

    val activity = LocalContext.current as MainActivity
    val provider = OAuthProvider.newBuilder("github.com")
    provider.setScopes(AuthRequestModel().generate().scopes)

    val localNavController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .fillMaxWidth(),
            contentDescription = "App Icon",
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            alignment = Alignment.Center
        )

        Button(
            onClick = {
                activity.lifecycleScope.launch {
                    signInWithGithub(
                        activity = activity,
                        provider = provider
                    ) { accessToken, login ->
                        val authManager = com.hoppers.networkmodule.network.AuthManager
                        authManager.saveAccessToken(accessToken, login)
                        localNavController.navigate(AppScreens.FEED.route)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }

        TextButton(
            onClick = { /* Handle skip */ },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.skip_login_now))
        }

        Text(
            text = stringResource(id = R.string.terms_condition),
            modifier = Modifier.padding(top = 16.dp),
            color = Color.White
        )
    }
}

private fun signInWithGithub(
    activity: MainActivity,
    provider: OAuthProvider.Builder,
    onSuccessfulSignIn: (String, String) -> Unit
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    if (currentUser != null && !com.hoppers.networkmodule.network.AuthManager.getAccessToken()
            .isNullOrEmpty()
    ) {
        return
    }
    val pendingResultTask = firebaseAuth.pendingAuthResult
    // There's something already here! Finish the sign-in for your user.
    pendingResultTask?.addOnSuccessListener { authResult ->
        val accessToken = (authResult.credential as OAuthCredential).accessToken
        val user = authResult.user?.providerData?.last()?.uid
        //  onSuccessfulSignIn(accessToken.toString())
    }?.addOnFailureListener {
        // Handle failure.
    } ?: // There's no pending result so you need to start the sign-in flow.
    firebaseAuth
        .startActivityForSignInWithProvider(activity, provider.build())
        .addOnSuccessListener { authResult ->
            // User is signed in.
            // IdP data available in
            val profileJson = (authResult.user?.providerData?.last() as zzab).zzb()
            val json = Json.parseToJsonElement(profileJson.toString()).jsonObject
            val rawUserInfoString = json["rawUserInfo"]?.jsonPrimitive?.content ?: ""

            // Parse the rawUserInfo JSON
            val rawUserInfoJson = Json.parseToJsonElement(rawUserInfoString).jsonObject

            val rawUserInfo: RawUserInfo = Json.decodeFromString(rawUserInfoJson.toString())
            val login = rawUserInfo.login
            // The OAuth access token can also be retrieved:
            val accessToken = (authResult.credential as OAuthCredential).accessToken
            onSuccessfulSignIn(accessToken.toString(), login)
        }
        .addOnFailureListener {}
}



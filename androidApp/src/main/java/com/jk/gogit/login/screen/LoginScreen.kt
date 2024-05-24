package com.jk.gogit.login.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.OAuthProvider
import com.jk.gogit.MainActivity
import com.jk.gogit.R
import com.jk.gogit.components.ComposeLocalWrapper
import com.jk.gogit.components.Page
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.login.AuthRequestModel
import com.jk.gogit.login.AuthViewModel
import com.jk.gogit.login.AuthenticationState
import com.jk.gogit.navigation.AppScreens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen() {
    val activity = LocalContext.current as MainActivity
    val provider = OAuthProvider.newBuilder("github.com")
    provider.setScopes(AuthRequestModel().generate().scopes)

    val localNavController = LocalNavController.current


    val authViewModel: AuthViewModel = koinViewModel<AuthViewModel>()

    val authenticationState by authViewModel.authenticationState.collectAsState()
    Page {
        when (authenticationState) {
            AuthenticationState.Initial ->

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                ) {
                    Image(
                        modifier = Modifier
                           // .heightIn(max = 300.dp)
                            .align(Alignment.CenterHorizontally),
                        contentDescription = "App Icon",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                        alignment = Alignment.Center,
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(
                        onClick = {
                            activity.lifecycleScope.launch {
                                authViewModel.signInWithGithub(
                                    activity = activity,
                                    provider = provider
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.sign_in))
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    val annotatedString = buildAnnotatedString {
                        pushStyle(style = ParagraphStyle(textAlign = TextAlign.Center))
                        append("Your login indicates acceptance of our ")
                        pushStringAnnotation(
                            tag = "PrivacyPolicy",
                            annotation = "https://jk2pr.github.io"
                        ) // Replace the URL with your actual Privacy Policy URL
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("\n")
                            append("Privacy Policy")
                        }
                        pop()
                    }


                    ClickableText(
                        modifier = Modifier
                           // .widthIn(max = 250.dp)
                            .align(Alignment.CenterHorizontally),
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "PrivacyPolicy",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let { annotation ->
                                    val uri = Uri.parse(annotation.item)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    startActivity(activity,intent, null)
                                   }
                        },
                        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.outline, fontSize = 14.sp),
                        maxLines = 2
                    )



                // Other UI elements

            }

            AuthenticationState.Authenticated ->
                localNavController.navigate(AppScreens.USERPROFILE.route) {
                    popUpTo(AppScreens.LOGIN.route) {
                        inclusive = true
                    }
                }


            AuthenticationState.AuthenticationFailed -> {
                // Handle authentication failure
                // You may display an error message to the user
            }

            AuthenticationState.Loading ->
                // Show loader
                CircularProgressIndicator(modifier = Modifier.size(50.dp))

        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {

    ComposeLocalWrapper {
        LoginScreen()
    }
}
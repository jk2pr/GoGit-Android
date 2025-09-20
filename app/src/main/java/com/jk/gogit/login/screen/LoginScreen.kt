package com.jk.gogit.login.screen

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
// import androidx.compose.ui.text.withStyle // Removed unused import
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
    val activity = LocalActivity.current as MainActivity
    val provider = OAuthProvider.newBuilder("github.com")
    provider.scopes = AuthRequestModel().generate().scopes

    val localNavController = LocalNavController.current


    val authViewModel: AuthViewModel = koinViewModel<AuthViewModel>()

    val authenticationState by authViewModel.authenticationState.collectAsState()
    Page {
        when (authenticationState) {
            AuthenticationState.Initial ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.Top
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(148.dp),
                        contentDescription = "App Icon",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                        alignment = Alignment.Center,
                    )

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
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.sign_in))
                    }

                    val privacyPolicyUrl = "https://jk2pr.github.io"
                    val preLinkText = "Your login indicates acceptance of our "
                    val linkText = "\nPrivacy Policy"

                    val annotatedString = buildAnnotatedString {
                        pushStyle(style = ParagraphStyle(textAlign = TextAlign.Center))
                        append(preLinkText)

                        val startIndex = length
                        append(linkText)
                        val endIndex = length

                        // Apply visual style to the link text
                        addStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline),
                            start = startIndex,
                            end = endIndex
                        )

                        // Create and add the LinkAnnotation
                        val clickableAnnotation = LinkAnnotation.Clickable(
                            tag = "PrivacyPolicyURL", // Semantic tag for the link
                            linkInteractionListener = object : LinkInteractionListener {
                                override fun onClick(link: LinkAnnotation) {
                                    val uri = privacyPolicyUrl.toUri()
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    activity.startActivity(intent)
                                }
                            }
                        )
                        addLink(
                            clickableAnnotation, // Corrected: pass annotation directly
                            start = startIndex,
                            end = endIndex
                        )
                        pop() // Pop the ParagraphStyle
                    }


                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = annotatedString,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp
                        ),
                        maxLines = 2
                    )


                    // Other UI elements

                }

           is AuthenticationState.Authenticated ->
                localNavController.navigate(AppScreens.HOME.route) {
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

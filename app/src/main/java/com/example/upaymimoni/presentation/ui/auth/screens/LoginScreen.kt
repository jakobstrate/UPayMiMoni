package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.components.AppLogo
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.ClickableText
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthLoginViewModel
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthLoginViewModel = koinViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToHomePage: () -> Unit,
    onNavigateToForgotPass: () -> Unit,
) {
    val email by authViewModel.email.collectAsState()
    val pass by authViewModel.pass.collectAsState()
    val error by authViewModel.errorMsg.collectAsState()
    val loading by authViewModel.loading.collectAsState()

    val uiEvent = authViewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.NavigateToHome -> {
                    onNavigateToHomePage()
                }
            }
        }
    }

    LoginContent(
        email = email,
        pass = pass,
        error = error,
        loading = loading,
        onEmailUpdate = authViewModel::updateEmail,
        onPassUpdate = authViewModel::updatePassword,
        onSignInClick = authViewModel::onSignInClick,
        onGoogleSignInClick = authViewModel::onGoogleSignIn,
        onSingUpClick = onNavigateToRegister,
        onForgotPassClick = onNavigateToForgotPass

    )
}

@Composable
fun LoginContent(
    email: TextFieldValue,
    pass: TextFieldValue,
    error: String? = null,
    loading: Boolean,
    onEmailUpdate: (TextFieldValue) -> Unit,
    onPassUpdate: (TextFieldValue) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onSingUpClick: () -> Unit,
    onForgotPassClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        AppLogo(
            modifier = Modifier
                .fillMaxWidth(3 / 4f)
                .wrapContentHeight(),
        )

        Spacer(
            modifier = Modifier.padding(44.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            UserInputField(
                label = stringResource(R.string.auth_email_label),
                placeHolder = stringResource(R.string.auth_email_placeholder),
                value = email,
                onValueChange = { onEmailUpdate(it) },
            )

            UserInputField(
                label = stringResource(R.string.auth_password_label),
                placeHolder = stringResource(R.string.auth_password_placeholder),
                value = pass,
                onValueChange = { onPassUpdate(it) },
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                text = stringResource(R.string.login_forgot_pass),
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(4.dp)
                    .clickable(onClick = onForgotPassClick)
            )

            ErrorDialogue(error)

            AuthButton(
                text = stringResource(R.string.login_button_text),
                onClick = { onSignInClick() },
                isLoading = loading
            )

            AuthButton(
                text = stringResource(R.string.login_google_button_text),
                onClick = { onGoogleSignInClick() },
                isLoading = loading
            )

        }


        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            normalText = stringResource(R.string.login_signup_noclick),
            clickText = stringResource(R.string.auth_signup_click),
            fontSize = 18.sp,
            onClick = onSingUpClick,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    UPayMiMoniTheme {
        LoginContent(
            email = TextFieldValue("example@gmail.com"),
            pass = TextFieldValue("ExamplePassword"),
            error = "No user was found for the given email, the user might have been deleted or something",
            loading = false,
            onEmailUpdate = {},
            onPassUpdate = {},
            onSignInClick = {},
            onGoogleSignInClick = {},
            onSingUpClick = {},
            onForgotPassClick = {}
        )
    }
}
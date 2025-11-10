package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.ClickableText
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
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
    val error by authViewModel.errorState.collectAsState()
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
    error: AuthErrorState,
    loading: Boolean,
    onEmailUpdate: (TextFieldValue) -> Unit,
    onPassUpdate: (TextFieldValue) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onSingUpClick: () -> Unit,
    onForgotPassClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.login_page_title),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.login_page_sub_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            UserInputField(
                value = email,
                label = stringResource(R.string.auth_email_label),
                placeHolder = stringResource(R.string.auth_email_placeholder),
                isError = error.emailError,
                onValueChange = { onEmailUpdate(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            ErrorDialogue(
                active = error.emailError,
                message = error.emailMsg
            )

            Spacer(modifier = Modifier.height(16.dp))

            UserInputField(
                value = pass,
                label = stringResource(R.string.auth_password_label),
                placeHolder = stringResource(R.string.auth_password_placeholder),
                onValueChange = { onPassUpdate(it) },
                isError = error.passwordError,
                isPasswordField = true,
            )

            ErrorDialogue(
                active = error.passwordError,
                message = error.passwordMsg
            )

            Text(
                text = stringResource(R.string.login_forgot_pass),
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 4.dp)
                    .clickable(onClick = onForgotPassClick)
            )



            Spacer(modifier = Modifier.height(64.dp))

            AuthButton(
                text = stringResource(R.string.login_button_text),
                onClick = onSignInClick,
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                   text = "or",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                )
            }

            ElevatedButton(
                onClick = onGoogleSignInClick,
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(242, 242, 242),
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google_icon_transparent),
                        contentDescription = "Google Login",
                        tint = Color.Unspecified,
                    )
                    Text(
                        text = stringResource(R.string.login_google_button_text),
                        style = MaterialTheme.typography.labelLarge,
                    )

                    Spacer(modifier = Modifier.width(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ClickableText(
                normalText = stringResource(R.string.login_signup_noclick),
                clickText = stringResource(R.string.auth_signup_click),
                onClick = onSingUpClick,
            )
        }
        
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    UPayMiMoniTheme {
        LoginContent(
            email = TextFieldValue("example@gmail.com"),
            pass = TextFieldValue("ExamplePassword"),
            error = AuthErrorState(emailError = true, passwordError = true, errorMsg = "This is an error message!"),
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
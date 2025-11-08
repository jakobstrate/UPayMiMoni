package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.components.AppLogo
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthRegisterViewModel
import com.example.upaymimoni.presentation.ui.auth.components.ClickableText
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthRegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToHomePage: () -> Unit,
) {
    val phone by authViewModel.phone.collectAsState()
    val email by authViewModel.email.collectAsState()
    val pass by authViewModel.pass.collectAsState()
    val error by authViewModel.errorState.collectAsState()
    val loading by authViewModel.loading.collectAsState()

    val uiEvent = authViewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.NavigateToHome -> onNavigateToHomePage()
            }
        }
    }

    RegisterScreenContent(
        phone,
        email,
        pass,
        error,
        loading,
        authViewModel::updatePhone,
        authViewModel::updateEmail,
        authViewModel::updatePassword,
        authViewModel::onRegisterClick,
        onNavigateToLogin
    )

}

@Composable
fun RegisterScreenContent(
    phone: TextFieldValue,
    email: TextFieldValue,
    pass: TextFieldValue,
    error: AuthErrorState,
    loading: Boolean,
    onPhoneUpdate: (TextFieldValue) -> Unit,
    onEmailUpdate: (TextFieldValue) -> Unit,
    onPassUpdate: (TextFieldValue) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {

}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    UPayMiMoniTheme {
        RegisterScreenContent(
            phone = TextFieldValue("+4588888888"),
            email = TextFieldValue("example@gmail.com"),
            pass =TextFieldValue("ExamplePassword"),
            error = AuthErrorState(
                errorMsg = "No user was found for the given email, the user might have been deleted or something"
            ),
            loading = false,
            onPhoneUpdate = {},
            onEmailUpdate = {},
            onPassUpdate = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
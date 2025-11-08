package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val name by authViewModel.name.collectAsState()
    val phone by authViewModel.number.collectAsState()
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
        name,
        phone,
        email,
        pass,
        error,
        loading,
        authViewModel::updateName,
        authViewModel::updatePhone,
        authViewModel::updateEmail,
        authViewModel::updatePassword,
        authViewModel::onRegisterClick,
        onNavigateToLogin
    )

}

@Composable
fun RegisterScreenContent(
    name: TextFieldValue,
    phone: TextFieldValue,
    email: TextFieldValue,
    pass: TextFieldValue,
    error: AuthErrorState,
    loading: Boolean,
    onNameUpdate: (TextFieldValue) -> Unit,
    onPhoneUpdate: (TextFieldValue) -> Unit,
    onEmailUpdate: (TextFieldValue) -> Unit,
    onPassUpdate: (TextFieldValue) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
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
                text = stringResource(R.string.register_page_title),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.register_page_sub_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name
            UserInputField(
                value = name,
                label = stringResource(R.string.register_name_label),
                placeHolder = stringResource(R.string.register_name_placeholder),
                isError = error.nameError,
                onValueChange = { onNameUpdate(it) },
            )

            ErrorDialogue(
                active = error.nameError,
                message = error.nameMsg
            )

            // Number
            UserInputField(
                value = phone,
                label = stringResource(R.string.register_number_label),
                placeHolder = stringResource(R.string.register_phone_placeholder),
                isError = error.numberError,
                onValueChange = { onPhoneUpdate(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            ErrorDialogue(
                active = error.numberError,
                message = error.numberMsg
            )

            // Email
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

            // Password
            UserInputField(
                value = pass,
                label = stringResource(R.string.auth_password_label),
                placeHolder = stringResource(R.string.auth_password_placeholder),
                isError = error.passwordError,
                isPasswordField = true,
                onValueChange = { onPassUpdate(it) }

            )

            ErrorDialogue(
                active = error.passwordError,
                message = error.passwordMsg
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(R.string.register_button_text),
                onClick = onRegisterClick,
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ClickableText(
                normalText = stringResource(R.string.register_login_page_noclick),
                clickText = stringResource(R.string.register_login_page_click),
                onClick = onLoginClick,
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
private fun RegisterScreenPreview() {
    UPayMiMoniTheme {
        RegisterScreenContent(
            name = TextFieldValue("Darth Vader"),
            phone = TextFieldValue("+4588888888"),
            email = TextFieldValue("example@gmail.com"),
            pass = TextFieldValue("ExamplePassword"),
            error = AuthErrorState(
                errorMsg = "No user was found for the given email, the user might have been deleted or something"
            ),
            loading = false,
            onNameUpdate = {},
            onPhoneUpdate = {},
            onEmailUpdate = {},
            onPassUpdate = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
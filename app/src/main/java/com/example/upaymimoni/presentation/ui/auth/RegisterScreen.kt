package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
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
import com.example.upaymimoni.presentation.ui.auth.components.AppLogo
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.AuthRegisterViewModel
import com.example.upaymimoni.presentation.ui.auth.components.ClickableText
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthRegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val phone by authViewModel.phone.collectAsState()
    val email by authViewModel.email.collectAsState()
    val pass by authViewModel.pass.collectAsState()
    val error by authViewModel.errorMsg.collectAsState()

    RegisterScreenContent(
        phone,
        email,
        pass,
        error,
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
    error: String? = null,
    onPhoneUpdate: (TextFieldValue) -> Unit,
    onEmailUpdate: (TextFieldValue) -> Unit,
    onPassUpdate: (TextFieldValue) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 24.dp)
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
                label = stringResource(R.string.register_phone_label),
                placeHolder = stringResource(R.string.register_phone_placeholder),
                value = phone,
                onValueChange = { onPhoneUpdate(it) },
            )

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

            ErrorDialogue(error)

            AuthButton(
                stringResource(R.string.register_button_text),
                onClick = { onRegisterClick() },
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            normalText = stringResource(R.string.register_login_page_noclick),
            clickText = stringResource(R.string.auth_signup_click),
            fontSize = 18.sp,
            onClick = onLoginClick,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    UPayMiMoniTheme {
        RegisterScreenContent(
            phone = TextFieldValue("+4588888888"),
            email = TextFieldValue("example@gmail.com"),
            pass =TextFieldValue("ExamplePassword"),
            error = "No user was found for the given email, the user might have been deleted or something",
            onPhoneUpdate = {},
            onEmailUpdate = {},
            onPassUpdate = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
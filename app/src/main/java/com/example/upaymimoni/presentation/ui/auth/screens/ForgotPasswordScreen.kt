package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthForgotPassViewModel
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPassScreen(
    forgotPassViewModel: AuthForgotPassViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val email by forgotPassViewModel.email.collectAsState()
    val loading by forgotPassViewModel.loading.collectAsState()
    val error by forgotPassViewModel.errorState.collectAsState()

    val showPopUp by forgotPassViewModel.showPopUp.collectAsState()

    ForgotPassScreenContent(
        onBackClick = onBackClick,
        email = email,
        onEmailUpdate = forgotPassViewModel::updateEmail,
        loading = loading,
        onSubmitClick = forgotPassViewModel::onSendResetEmailClick,
        error = error,
        showPopUp = showPopUp,
        removePopUp = forgotPassViewModel::removePopUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassScreenContent(
    onBackClick: () -> Unit,
    email: TextFieldValue,
    onEmailUpdate: (TextFieldValue) -> Unit,
    loading: Boolean,
    onSubmitClick: () -> Unit,
    error: AuthErrorState,
    showPopUp: Boolean,
    removePopUp: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.reset_pass_bar_title),
                        style = MaterialTheme.typography.headlineMedium

                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 48.dp, horizontal = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LockReset,
                contentDescription = "Reset Password Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.reset_pass_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.reset_pass_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            UserInputField(
                value = email,
                label = stringResource(R.string.auth_email_label),
                placeHolder = stringResource(R.string.auth_email_placeholder),
                isError = error.emailError,
                onValueChange = onEmailUpdate,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            ErrorDialogue(
                active = error.emailError,
                message = error.emailMsg
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthButton(
                text = stringResource(R.string.reset_pass_submit_button),
                onClick = onSubmitClick,
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            )

            if (showPopUp) {
                ResetEmailPopUp(
                    email = email.text,
                    onDismiss = {
                        removePopUp()
                        onBackClick()
                    }
                )
            }
        }
    }
}

@Composable
fun ResetEmailPopUp(
    email: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.popup_button))
            }
        },
        title = { Text(stringResource(R.string.popup_title)) },
        text = {
            Text(
                text = stringResource(R.string.popup_body, email),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        },
        modifier = Modifier.padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ForgotPassScreenPreview(

) {
    UPayMiMoniTheme {
        ForgotPassScreenContent(
            onBackClick = {},
            email = TextFieldValue("example@test.com"),
            onEmailUpdate = {},
            loading = false,
            onSubmitClick = {},
            error = AuthErrorState(
                errorMsg = "Please fill in the required fields."
            ),
            showPopUp = true,
            removePopUp = {}
        )
    }
}
package com.example.upaymimoni.presentation.ui.auth.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.ErrorDialogue
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
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
    val error by forgotPassViewModel.errorMsg.collectAsState()

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

@Composable
fun ForgotPassScreenContent(
    onBackClick: () -> Unit,
    email: TextFieldValue,
    onEmailUpdate: (TextFieldValue) -> Unit,
    loading: Boolean,
    onSubmitClick: () -> Unit,
    error: String?,
    showPopUp: Boolean,
    removePopUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ForgotPasswordHeader(
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            UserInputField(
                label = stringResource(R.string.auth_email_label),
                placeHolder = stringResource(R.string.auth_email_placeholder),
                value = email,
                onValueChange = { onEmailUpdate(it) }
            )

            ErrorDialogue(error)

            AuthButton(
                text = stringResource(R.string.reset_pass_submit_button),
                onClick = onSubmitClick,
                isLoading = loading
            )
        }

    }

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

@Composable
fun ForgotPasswordHeader(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text =  stringResource(R.string.reset_pass_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
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
                text = stringResource(R.string.popup_body, email)
            )
        },
        modifier = Modifier.padding(16.dp)
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
            error = "Please fill in the required fields.",
            showPopUp = true,
            removePopUp = {}
        )
    }
}
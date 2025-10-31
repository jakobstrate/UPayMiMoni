package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.R
import com.example.upaymimoni.presentation.ui.auth.components.AppLogo
import com.example.upaymimoni.presentation.ui.auth.components.AuthButton
import com.example.upaymimoni.presentation.ui.auth.components.ClickableText
import com.example.upaymimoni.presentation.ui.auth.components.UserInputField
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun LoginScreen() {
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
                label = stringResource(R.string.login_email_label),
                placeHolder = stringResource(R.string.login_email_placeholder),
                value = TextFieldValue("Placeholder for testing"),
                onValueChange = {  },
            )

            UserInputField(
                label = stringResource(R.string.login_password_label),
                placeHolder = stringResource(R.string.login_password_placeholder),
                value = TextFieldValue("Placeholder for testing"),
                onValueChange = {  },
            )

            AuthButton(
                stringResource(R.string.login_button_text),
                onClick = { }
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            normalText = stringResource(R.string.login_signup_noclick),
            clickText = stringResource(R.string.login_signup_click),
            fontSize = 18.sp,
            onClick = { },
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    UPayMiMoniTheme {
        LoginScreen()
    }
}
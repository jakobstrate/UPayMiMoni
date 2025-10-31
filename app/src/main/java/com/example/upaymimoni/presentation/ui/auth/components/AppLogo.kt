package com.example.upaymimoni.presentation.ui.auth.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.example.upaymimoni.R

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.app_logo),
        contentDescription = "Default App Icon",
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    UPayMiMoniTheme {
        AppLogo()
    }
}
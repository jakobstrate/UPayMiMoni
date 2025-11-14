package com.example.upaymimoni.presentation.ui.auth.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthDestination
import com.example.upaymimoni.presentation.ui.auth.viewmodel.InitialLoadingViewModel
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun InitialLoadingScreen(
    initialLoadingViewModel: InitialLoadingViewModel = koinViewModel(),
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    val authCheckEvent = initialLoadingViewModel.authCheckEvent

    LaunchedEffect(Unit) {
        authCheckEvent.collect { destination ->
            when (destination) {
                AuthDestination.Authenticated -> onAuthenticated()
                AuthDestination.Unauthenticated -> {
                    Log.d("AutoAuth", "User was not authenticated. Navigation to auth.")
                    onUnauthenticated()
                }
            }
        }
    }

    InitialLoadingContent()
}

@Composable
fun InitialLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun InitialLoadingScreenPreview() {
    UPayMiMoniTheme {
        InitialLoadingContent()
    }
}

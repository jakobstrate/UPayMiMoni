package com.example.upaymimoni.presentation.ui.auth.utils

sealed class AuthUiEvent {
    object NavigateToHome : AuthUiEvent()
    data class ShowSnackbar(val message: String) : AuthUiEvent()
}
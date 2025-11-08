package com.example.upaymimoni.presentation.ui.auth.utils

data class AuthErrorState(
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val googleError: Boolean = false,
    val errorMsg: String? = null,
)

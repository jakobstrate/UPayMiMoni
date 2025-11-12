package com.example.upaymimoni.presentation.ui.auth.utils

data class AuthErrorState(
    val nameError: Boolean = false,
    val nameMsg: String? = null,

    val numberError: Boolean = false,
    val numberMsg: String? = null,

    val emailError: Boolean = false,
    val emailMsg: String? = null,

    val passwordError: Boolean = false,
    val passwordMsg: String? = null,

    val errorMsg: String? = null,
)

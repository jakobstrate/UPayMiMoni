package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirebaseAuthRepository
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.usecase.LoginUseCase
import org.koin.core.module.dsl.viewModel
import com.example.upaymimoni.presentation.ui.auth.AuthLoginViewModel
import com.example.upaymimoni.presentation.ui.auth.components.AuthRegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }

    single <AuthRepository> { FirebaseAuthRepository(get()) }

    factory { LoginUseCase(get()) }

    viewModel {
        AuthLoginViewModel(get())
    }

    viewModel {
        AuthRegisterViewModel()
    }
}
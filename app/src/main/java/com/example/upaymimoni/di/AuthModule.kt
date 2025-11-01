package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirebaseAuthRepository
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.usecase.LoginUseCase
import com.example.upaymimoni.domain.usecase.RegisterUseCase
import org.koin.core.module.dsl.viewModel
import com.example.upaymimoni.presentation.ui.auth.AuthLoginViewModel
import com.example.upaymimoni.presentation.ui.auth.AuthRegisterViewModel
import com.example.upaymimoni.presentation.ui.auth.UiMessageTranslation
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }

    single <AuthRepository> { FirebaseAuthRepository(get()) }

    single { UiMessageTranslation() }

    factory { LoginUseCase(get()) }

    factory { RegisterUseCase(get()) }

    viewModel {
        AuthLoginViewModel(get(), get())
    }

    viewModel {
        AuthRegisterViewModel(get(), get())
    }
}
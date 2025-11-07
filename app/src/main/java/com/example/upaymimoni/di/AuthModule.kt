package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirebaseAuthRepository
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.usecase.auth.GoogleLoginUseCase
import com.example.upaymimoni.domain.usecase.auth.LoginUseCase
import com.example.upaymimoni.domain.usecase.auth.RegisterUseCase
import com.example.upaymimoni.domain.usecase.auth.ResetPasswordUseCase
import org.koin.core.module.dsl.viewModel
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthLoginViewModel
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthRegisterViewModel
import com.example.upaymimoni.presentation.ui.auth.utils.GoogleSignInClient
import com.example.upaymimoni.presentation.ui.auth.utils.UiMessageTranslation
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthForgotPassViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }

    single { GoogleSignInClient(androidContext()) }

    single<AuthRepository> { FirebaseAuthRepository(get()) }

    single { UiMessageTranslation() }

    factory { LoginUseCase(get()) }

    factory { RegisterUseCase(get(), get(), get()) }

    factory { GoogleLoginUseCase(get(), get()) }

    factory { ResetPasswordUseCase(get()) }

    viewModel {
        AuthLoginViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        AuthRegisterViewModel(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        AuthForgotPassViewModel(
            get(),
            get()
        )
    }
}
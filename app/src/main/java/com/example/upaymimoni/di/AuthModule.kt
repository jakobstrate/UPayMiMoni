package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirebaseAuthRepository
import com.example.upaymimoni.data.state.FirebaseAuthStateProvider
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.state.AuthStateProvider
import com.example.upaymimoni.domain.usecase.auth.GoogleLoginUseCase
import com.example.upaymimoni.domain.usecase.auth.LoginUseCase
import com.example.upaymimoni.domain.usecase.auth.LogoutUseCase
import com.example.upaymimoni.domain.usecase.auth.RegisterUseCase
import com.example.upaymimoni.domain.usecase.auth.ResetPasswordUseCase
import org.koin.core.module.dsl.viewModel
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthLoginViewModel
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthRegisterViewModel
import com.example.upaymimoni.presentation.ui.auth.utils.GoogleSignInClient
import com.example.upaymimoni.presentation.ui.auth.viewmodel.AuthForgotPassViewModel
import com.example.upaymimoni.presentation.ui.auth.viewmodel.InitialLoadingViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }

    single { GoogleSignInClient(androidContext()) }

    single<AuthRepository> {
        FirebaseAuthRepository(
            get(),
            get(named("UpdateUserMapper")),
            get(named("AuthMapper"))
        )
    }

    single<AuthStateProvider> {
        FirebaseAuthStateProvider(
            get()
        )
    }

    factory { LoginUseCase(get(), get(), get()) }

    factory { RegisterUseCase(get(), get(), get(), get()) }

    factory { GoogleLoginUseCase(get(), get(), get()) }

    factory { ResetPasswordUseCase(get()) }

    factory { LogoutUseCase(get(), get()) }

    viewModel {
        AuthLoginViewModel(
            get(),
            get(),
            get(),
        )
    }

    viewModel {
        AuthRegisterViewModel(
            get(),
        )
    }

    viewModel {
        AuthForgotPassViewModel(
            get()
        )
    }

    viewModel {
        InitialLoadingViewModel(
            get(),
            get(),
            get()
        )
    }
}
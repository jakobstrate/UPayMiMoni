package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirestoreUserRepository
import com.example.upaymimoni.data.session.UserSessionImpl
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.example.upaymimoni.domain.usecase.user.SaveUserUseCase
import org.koin.dsl.module

val ApplicationModule = module {
    single <UserSession> {
        UserSessionImpl()
    }

    single < UserRepository> {
        FirestoreUserRepository()
    }

    factory { GetUserUseCase(get()) }

    factory { SaveUserUseCase(get()) }
}
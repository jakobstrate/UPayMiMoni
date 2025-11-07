package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.DiceBearAvatarRepository
import com.example.upaymimoni.data.repository.FirestoreUserRepository
import com.example.upaymimoni.data.session.UserSessionImpl
import com.example.upaymimoni.domain.repository.AvatarRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val ApplicationModule = module {

    single { FirebaseFirestore.getInstance() }
    single <UserSession> {
        UserSessionImpl()
    }

    single <UserRepository> {
        FirestoreUserRepository(get())
    }

    single <AvatarRepository> {
        DiceBearAvatarRepository()
    }

    factory { GetUserUseCase(get()) }

}
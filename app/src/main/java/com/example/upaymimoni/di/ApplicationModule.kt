package com.example.upaymimoni.di

import com.example.upaymimoni.data.mappers.ErrorMapper
import com.example.upaymimoni.data.mappers.FirebaseAuthErrorMapper
import com.example.upaymimoni.data.mappers.FirebaseUpdateUserErrorMapper
import com.example.upaymimoni.data.repository.DiceBearAvatarRepository
import com.example.upaymimoni.data.repository.FirebaseAuthRepository
import com.example.upaymimoni.data.repository.FirebaseProfilePictureProfilePictureStorageRepository
import com.example.upaymimoni.data.repository.FirestoreFcmTokenRepository
import com.example.upaymimoni.data.repository.FirestoreUserRepository
import com.example.upaymimoni.data.service.DeviceIdService
import com.example.upaymimoni.data.service.FcmTokenManager
import com.example.upaymimoni.data.session.UserSessionImpl
import com.example.upaymimoni.data.state.FirebaseAuthStateProvider
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.AvatarRepository
import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.example.upaymimoni.domain.repository.ProfilePictureStorageRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.service.TokenManager
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.state.AuthStateProvider
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ApplicationModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { DeviceIdService(androidContext()) }

    single<FcmTokenRepository> {
        FirestoreFcmTokenRepository(get())
    }

    single<TokenManager> {
        FcmTokenManager(
            get(),
            get(),
            get(),
        )
    }

    single<UserSession> {
        UserSessionImpl()
    }

    single<ErrorMapper<Exception, UpdateUserError>>(named("UpdateUserMapper")) { FirebaseUpdateUserErrorMapper() }
    single<ErrorMapper<Exception, AuthError>>(named("AuthMapper")) { FirebaseAuthErrorMapper() }

    single<UserRepository> {
        FirestoreUserRepository(
            get(),
            get(named("UpdateUserMapper"))
        )
    }

    single<ProfilePictureStorageRepository> {
        FirebaseProfilePictureProfilePictureStorageRepository(
            get()
        )
    }

    single<AvatarRepository> {
        DiceBearAvatarRepository()
    }

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

    factory { GetUserUseCase(get()) }


}
package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.PushNotificationApi
import com.example.upaymimoni.data.repository.PushNotificationRepositoryImpl
import com.example.upaymimoni.domain.repository.PushNotificationRepository
import com.example.upaymimoni.domain.usecase.notification.SendNotificationToUserUseCase
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val ApiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(PushNotificationApi::class.java)
    }

    single<PushNotificationRepository> {
        PushNotificationRepositoryImpl(get())
    }

    factory { SendNotificationToUserUseCase(get(), get()) }
}
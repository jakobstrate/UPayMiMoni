package com.example.upaymimoni.di

import com.example.upaymimoni.data.session.UserSessionImpl
import com.example.upaymimoni.domain.session.UserSession
import org.koin.dsl.module

val ApplicationModule = module {
    single <UserSession> {
        UserSessionImpl()
    }
}
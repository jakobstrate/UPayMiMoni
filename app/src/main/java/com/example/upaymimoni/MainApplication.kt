package com.example.upaymimoni

import android.app.Application
import com.example.upaymimoni.di.ApiModule
import com.example.upaymimoni.di.ApplicationModule
import com.example.upaymimoni.di.ProfileModule
import com.example.upaymimoni.di.authModule
import com.example.upaymimoni.di.expenseAddModule
import com.example.upaymimoni.di.expenseDetailModule
import com.example.upaymimoni.di.groupEditModule
import com.example.upaymimoni.di.groupModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(
                ApplicationModule,
                expenseDetailModule,
                expenseAddModule,
                authModule,
                ProfileModule,
                ApiModule,
                groupModule,
                groupEditModule,
            ))
        }
    }
}
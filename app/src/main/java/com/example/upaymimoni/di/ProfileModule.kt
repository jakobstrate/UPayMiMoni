package com.example.upaymimoni.di

import com.example.upaymimoni.domain.usecase.user.UpdateUserUseCase
import com.example.upaymimoni.domain.usecase.user.UploadProfilePictureUseCase
import com.example.upaymimoni.presentation.ui.profile.viewmodel.EditProfileViewModel
import com.example.upaymimoni.presentation.ui.profile.viewmodel.ProfileViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val ProfileModule = module {
    factory { UpdateUserUseCase(get(), get(), get()) }
    factory { UploadProfilePictureUseCase(get(), get(), get()) }

    viewModel {
        ProfileViewModel(get(), get())
    }
    viewModel {
        EditProfileViewModel(get(), get(), get())
    }
}

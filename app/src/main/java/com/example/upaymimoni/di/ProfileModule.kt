package com.example.upaymimoni.di

import com.example.upaymimoni.presentation.ui.profile.viewmodel.EditProfileViewModel
import com.example.upaymimoni.presentation.ui.profile.viewmodel.ProfileViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val ProfileModule = module {
    viewModel {
        ProfileViewModel(get(), get())
    }
    viewModel {
        EditProfileViewModel(get())
    }
}

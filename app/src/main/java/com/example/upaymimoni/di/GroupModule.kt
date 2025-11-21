package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirestoreGroupRepository
import com.example.upaymimoni.domain.repository.GroupRepository

import com.example.upaymimoni.data.repository.FirestoreExpenseRepository
import com.example.upaymimoni.data.repository.MockExpenseRepository
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.usecase.expense.GetExpenseDetailUseCase
import com.example.upaymimoni.domain.usecase.groups.CalculateSettlementUseCase
import com.example.upaymimoni.domain.usecase.user.UploadProfilePictureUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseDetailViewModel
import com.example.upaymimoni.presentation.ui.groups.viewmodel.GroupRedirectorViewModel
import com.example.upaymimoni.presentation.ui.groups.viewmodel.SettledGroupViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Module setup for dependency injection (DI).
 * This defines how the app creates and provides instances of Repositories, Use Cases, and ViewModels.
 */
val groupModule = module {

    factory { CalculateSettlementUseCase() }
    single<GroupRepository> {
        FirestoreGroupRepository(get())
    }

    //Domain Layer
    factory { GetExpenseDetailUseCase(get()) }
    factory { UploadProfilePictureUseCase(get()) }

    // Presentation Layer
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (id: String) ->
        ExpenseDetailViewModel(
            get(), TODO(),
            expenseId = TODO(),
            getUserUseCase = TODO(),
            getGroupUseCase = TODO()
        )
    }

    viewModel {
        GroupRedirectorViewModel(get())
    }

    viewModel {
        SettledGroupViewModel(get(), get(), get(), get(), get(), get())
    }

}
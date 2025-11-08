package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.ExpenseRepository
import com.example.upaymimoni.data.repository.MockExpenseRepository
import com.example.upaymimoni.domain.usecase.AddExpenseUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseAddViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Module setup for dependency injection (DI).
 * This defines how the app creates and provides instances of Repositories, Use Cases, and ViewModels.
 */
val expenseAddModule = module {
    // Data Layer: Repository
    // The single() keyword creates a singleton instance of the repository.
    single<ExpenseRepository> {
        MockExpenseRepository()
        // Replace with: FirestoreExpenseRepository(get())
    }

    single {
        AddExpenseUseCase(get())
    }
    // Presentation Layer: ViewModel
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (groupId: String,userId: String) ->
        ExpenseAddViewModel(
            get(), id,
            userId
        )
    }
}
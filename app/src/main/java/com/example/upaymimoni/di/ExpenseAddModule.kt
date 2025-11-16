package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirebaseAttachmentStorageRepository
import com.example.upaymimoni.data.repository.FirestoreExpenseRepository
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.data.repository.MockExpenseRepository
import com.example.upaymimoni.domain.repository.AttachmentStorageRepository
import com.example.upaymimoni.domain.usecase.expense.AddExpenseUseCase
import com.example.upaymimoni.domain.usecase.expense.AddExpenseWithAttachmentUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseAddViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Module setup for dependency injection (DI).
 * This defines how the app creates and provides instances of Repositories, Use Cases, and ViewModels.
 */
val expenseAddModule = module {
    // Data Layer
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    single<ExpenseRepository> {
        //MockExpenseRepository()
        FirestoreExpenseRepository(get())
    }
    single<AttachmentStorageRepository> {
        FirebaseAttachmentStorageRepository(androidApplication(), get())
    }

    //Domain Layer
    factory { AddExpenseUseCase(get())}

    factory {
        AddExpenseWithAttachmentUseCase(get(), get())
    }

    // Presentation Layer
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (groupId: String,userId: String) ->
        ExpenseAddViewModel(
            get(),
            groupId,
            userId
        )
    }


}
package com.example.upaymimoni.di

import com.example.upaymimoni.data.mappers.ErrorMapper
import com.example.upaymimoni.data.mappers.FirebaseUpdateUserErrorMapper
import com.example.upaymimoni.data.repository.FirestoreExpenseRepository
import com.example.upaymimoni.data.repository.FirestoreGroupRepository
import com.example.upaymimoni.data.repository.FirestoreUserRepository
import com.example.upaymimoni.data.repository.MockExpenseRepository
import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.usecase.expense.GetExpenseDetailUseCase
import com.example.upaymimoni.domain.usecase.expense.RemoveExpenseUseCase
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseDetailViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin Module setup for dependency injection (DI).
 * This defines how the app creates and provides instances of Repositories, Use Cases, and ViewModels.
 */
val expenseDetailModule = module {
    single<ExpenseRepository> {
        //MockExpenseRepository()
        FirestoreExpenseRepository(get())
    }
    single<ErrorMapper<Exception, UpdateUserError>>(named("UpdateUserMapper")) { FirebaseUpdateUserErrorMapper() }

    single<UserRepository> {
        FirestoreUserRepository(get(),get(named("UpdateUserMapper")))
    }

    single<GroupRepository> {
        //MockExpenseRepository()
        FirestoreGroupRepository(get())
    }

    //Domain Layer
    factory { RemoveExpenseUseCase(get()) }
    factory { GetExpenseDetailUseCase(get())}
    factory { GetUserUseCase(get())}
    factory { GetGroupUseCase(get())}

    // Presentation Layer
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (id: String) ->
        ExpenseDetailViewModel(
            get(),
            get(),
            get(),
            get(),
            id)
    }
}
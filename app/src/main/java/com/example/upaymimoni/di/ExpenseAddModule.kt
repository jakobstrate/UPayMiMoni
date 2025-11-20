package com.example.upaymimoni.di

import com.example.upaymimoni.data.mappers.ErrorMapper
import com.example.upaymimoni.data.mappers.FirebaseUpdateUserErrorMapper
import com.example.upaymimoni.data.repository.FirebaseAttachmentStorageRepository
import com.example.upaymimoni.data.repository.FirestoreExpenseRepository
import com.example.upaymimoni.data.repository.FirestoreGroupRepository
import com.example.upaymimoni.data.repository.FirestoreUserRepository
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.data.repository.MockExpenseRepository
import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.repository.AttachmentStorageRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.usecase.expense.AddExpenseUseCase
import com.example.upaymimoni.domain.usecase.expense.AddExpenseWithAttachmentUseCase
import com.example.upaymimoni.domain.usecase.expense.GetExpenseAddUIDataUseCase
import com.example.upaymimoni.domain.usecase.groups.GetAllUsersFromGroupUseCase
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseAddViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
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
    single<GroupRepository> {
        //MockExpenseRepository()
        FirestoreGroupRepository(get())
    }
    single<ErrorMapper<Exception, UpdateUserError>>(named("UpdateUserMapper")) { FirebaseUpdateUserErrorMapper() }

    single<UserRepository> {
        FirestoreUserRepository(get(),get(named("UpdateUserMapper")))
    }
    single<AttachmentStorageRepository> {
        FirebaseAttachmentStorageRepository(androidApplication(), get())
    }

    //Domain Layer
    factory { AddExpenseUseCase(get(),get())}

    factory {
        AddExpenseWithAttachmentUseCase(get(), get(),get())
    }

    factory { GetUserUseCase(get())}
    factory { GetGroupUseCase(get())}

    factory { GetAllUsersFromGroupUseCase(
        get(),
        get()
    )}

    factory { GetExpenseAddUIDataUseCase(get(),get())}

    // Presentation Layer
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (groupId: String,userId: String) ->
        ExpenseAddViewModel(
            get(),
            get(),
            groupId,
            userId
        )
    }


}
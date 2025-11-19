package com.example.upaymimoni.di

import com.example.upaymimoni.data.repository.FirestoreGroupRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.domain.usecase.groups.UpdateGroupUseCase
import com.example.upaymimoni.presentation.ui.groups.EditGroupViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val groupEditModule = module {

    // Data Layer
    single<GroupRepository> {
        FirestoreGroupRepository(get<FirebaseFirestore>())
    }

    //Domain Layer
    factory { GetGroupUseCase(get()) }
    factory { UpdateGroupUseCase(get()) }

    // Presentation Layer
    // The viewModel() keyword is Koin's specific way to create a ViewModel instance.
    viewModel { (groupId: String) ->
        EditGroupViewModel(
            getGroupUseCase = get(),
            updateGroupUseCase = get(),
            groupId = groupId
        )
    }
}
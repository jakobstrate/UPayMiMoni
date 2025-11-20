package com.example.upaymimoni.domain.usecase.expense

import com.example.upaymimoni.domain.usecase.groups.GetAllUsersFromGroupUseCase
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseAddUIData
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.UserUIData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetExpenseAddUIDataUseCase (
    private val getGroupByIdUseCase: GetGroupUseCase,
    private val getAllUsersUseCase: GetAllUsersFromGroupUseCase
    ) {
        suspend operator fun invoke(groupId: String): Result<ExpenseAddUIData> = coroutineScope {
            val groupAsynced = async { getGroupByIdUseCase(groupId) }
            val usersAsynced = async { getAllUsersUseCase(groupId) }

            val groupResult = groupAsynced.await()
            val usersResult = usersAsynced.await()

            if (groupResult.isFailure) {
                return@coroutineScope Result.failure(groupResult.exceptionOrNull() ?: Exception("Failed to load group data."))
            }
            if (usersResult.isFailure) {
                return@coroutineScope Result.failure(usersResult.exceptionOrNull() ?: Exception("Failed to load user list."))
            }

            val group = groupResult.getOrThrow()
            val users = usersResult.getOrThrow()
            // Map domain models User to UI models UserUIData
            val userOptions = users.map { user ->
                UserUIData(id = user.id, name = user.displayName!!)
            }

            //just to make me not have cancer with find()
            val userIdToNameMap: Map<String, String> = userOptions.associate { user ->
                user.id to user.name
            }

            val uiData = ExpenseAddUIData(
                groupId = group.id,
                groupName = group.groupName,
                userOptions = userOptions,
                userIdToNameMap = userIdToNameMap
            )

            Result.success(uiData)
        }
    }
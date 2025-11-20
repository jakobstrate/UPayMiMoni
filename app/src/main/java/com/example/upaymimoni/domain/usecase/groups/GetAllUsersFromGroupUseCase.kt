package com.example.upaymimoni.domain.usecase.groups

import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.GroupRepository
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase

/**
 * get all users from a group
 */
class GetAllUsersFromGroupUseCase (
    private val groupRepository: GroupRepository,
    private val getUserUseCase: GetUserUseCase
) {
    suspend operator fun invoke(groupId: String): Result<List<User>> {
        val result = groupRepository.getGroup(groupId)

        return result.fold(
            onSuccess = { group ->
                val userIds = group.members

                // List of successfully fetched users
                val userList = mutableListOf<User>()

                // Iterate over IDs
                for (userId in userIds!!) {
                    val userResult = getUserUseCase(userId)

                    userResult.fold(
                        onSuccess = { user ->
                            //add to list
                            userList.add(user)
                        },
                        onFailure = { exception ->
                            // one for all and all for one
                            return Result.failure(exception)
                        }
                    )
                }
                Result.success(userList.toList())
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}
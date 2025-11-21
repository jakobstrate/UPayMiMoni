package com.example.upaymimoni.domain.usecase.groups

import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.repository.GroupRepository
import kotlinx.coroutines.delay

class GetGroupUseCase (private val groupRepository: GroupRepository) {
    suspend operator fun invoke(groupId: String): Result<Group> {
        return groupRepository.getGroup(groupId);
    }
}
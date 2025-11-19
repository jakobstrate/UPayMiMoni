package com.example.upaymimoni.domain.usecase.groups

import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.repository.GroupRepository

class UpdateGroupUseCase(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(group: Group): Result<Unit> {
        return groupRepository.saveGroup(group)
    }
}
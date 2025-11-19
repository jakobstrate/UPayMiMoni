package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.Group

interface GroupRepository {
    suspend fun saveGroup(group: Group): Result<Unit>
    suspend fun getGroup(groupId: String): Result<Group>

}
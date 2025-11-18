package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.Group

interface GroupRepository {
    fun getGroupById(id: String) : Result<Group>
}
package com.example.upaymimoni.data.repository

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.UserRepository

class FirestoreUserRepository(

) : UserRepository {
    override fun saveUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): Result<User> {
        TODO("Not yet implemented")
    }
}
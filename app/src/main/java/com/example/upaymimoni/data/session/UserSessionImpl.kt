package com.example.upaymimoni.data.session

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionImpl : UserSession {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser = _currentUser.asStateFlow()

    override fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    override fun clearUser() {
        _currentUser.value = null
    }
}
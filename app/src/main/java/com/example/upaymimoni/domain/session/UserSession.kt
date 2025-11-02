package com.example.upaymimoni.domain.session

import com.example.upaymimoni.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserSession {
    /**
     * We allow null as that will be our state when no user is logged in.
     *
     * @param User The current logged in user, null when none is logged in.
     */
    val currentUser: StateFlow<User?>
    fun setCurrentUser(user: User)
    fun clearUser()
}
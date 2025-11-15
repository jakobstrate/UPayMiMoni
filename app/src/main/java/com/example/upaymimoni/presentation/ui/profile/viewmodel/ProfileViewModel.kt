package com.example.upaymimoni.presentation.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class LogoutEvents {
    data object NavigateToLogin : LogoutEvents()
}

class ProfileViewModel(
    private val userSession: UserSession,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    val currentUser: StateFlow<User?> = userSession.currentUser

    private val _uiEvent = MutableSharedFlow<LogoutEvents>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onLogoutClick() = viewModelScope.launch {
        logoutUseCase()
        _uiEvent.emit(LogoutEvents.NavigateToLogin)
    }
}
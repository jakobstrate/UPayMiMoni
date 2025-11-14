package com.example.upaymimoni.presentation.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.state.AuthStateProvider
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class AuthDestination {
    object Authenticated : AuthDestination()
    object Unauthenticated : AuthDestination()
}
class InitialLoadingViewModel(
    private val authStateProvider: AuthStateProvider,
    private val userSession: UserSession,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {
    /**
     * Internal event that emits an @see AuthDestination.
     *
     * We need replay = 1 as we trigger the event in init before any collectors are registered.
     * By setting this we replay the event when a new collectors is registered.
     * This also means this event should not be subscribed to retroactively in the application.
     */
    private val _authCheckEvent = MutableSharedFlow<AuthDestination>(replay = 1)
    val authCheckEvent = _authCheckEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            checkAuthState()
        }
    }

    private suspend fun checkAuthState() {
        val isAuthenticated = authStateProvider.isAuthenticated()

        if (!isAuthenticated) {
            Log.d("AutoAuth", "User is not authenticated at startup.")
            _authCheckEvent.emit(AuthDestination.Unauthenticated)
            return
        }

        val userId = authStateProvider.getAuthenticatedUserId()
        if (userId == null) {
            Log.w("AutoAuth", "User was authenticated, but failed to get its id.")
            _authCheckEvent.emit(AuthDestination.Unauthenticated)
            return
        }

        val user = getUserUseCase(userId)
        user.fold(
            onSuccess = {
                Log.d("AutoAuth", "User was authenticated, and a user was found.")
                userSession.setCurrentUser(it)
                _authCheckEvent.emit(AuthDestination.Authenticated)
            },
            onFailure = {
                Log.w("AutoAuth", "User was authenticated, but failed to retrieve user from storage,")
                _authCheckEvent.emit(AuthDestination.Unauthenticated)
            }
        )
    }
}
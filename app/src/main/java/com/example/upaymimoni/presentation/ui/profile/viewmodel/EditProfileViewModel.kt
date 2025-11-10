package com.example.upaymimoni.presentation.ui.profile.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditProfileViewModel(
    private val userSession: UserSession,
) : ViewModel() {
    val currentUser: StateFlow<User?> = userSession.currentUser

    private val _newName = MutableStateFlow(TextFieldValue(""))
    val newName: StateFlow<TextFieldValue> = _newName

    private val _newEmail = MutableStateFlow(TextFieldValue(""))
    val newEmail: StateFlow<TextFieldValue> = _newEmail

    private val _newPhone = MutableStateFlow(TextFieldValue(""))
    val newPhone: StateFlow<TextFieldValue> = _newPhone

    fun initializeUser(user: User) {
        _newName.value = TextFieldValue(user.displayName ?: "")
        _newEmail.value = TextFieldValue(user.email ?: "")
        _newPhone.value = TextFieldValue(user.phoneNumber ?: "")
    }

    fun updateName(updatedName: TextFieldValue) {
        _newName.value = updatedName
    }

    fun updateEmail(updatedEmail: TextFieldValue) {
        _newEmail.value = TextFieldManipulator.clearWhiteSpaceFromField(updatedEmail)
    }

    fun updatePhone(updatedPhone: TextFieldValue) {
        _newPhone.value = TextFieldManipulator.removeAllNonNumerics(updatedPhone)
    }
}
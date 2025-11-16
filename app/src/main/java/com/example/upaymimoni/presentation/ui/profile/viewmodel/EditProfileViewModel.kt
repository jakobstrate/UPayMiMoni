package com.example.upaymimoni.presentation.ui.profile.viewmodel

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.model.result.UserUpdateResult
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.user.UpdateUserUseCase
import com.example.upaymimoni.domain.usecase.user.UploadProfilePictureUseCase
import com.example.upaymimoni.presentation.ui.utils.FieldType
import com.example.upaymimoni.presentation.ui.utils.UpdateUserErrorMapper
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SaveChangesEvents {
    data object NavigateToProfile : SaveChangesEvents()
    data class ShowSnackbar(val message: String) : SaveChangesEvents()
}

data class ErrorState(
    val nameError: Boolean = false,
    val numberError: Boolean = false,
    val emailError: Boolean = false,

    val errorMsg: String? = null,
)

class EditProfileViewModel(
    private val userSession: UserSession,
    private val updateUserUseCase: UpdateUserUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase
) : ViewModel() {
    val currentUser: StateFlow<User?> = userSession.currentUser

    private val _newName = MutableStateFlow(TextFieldValue(""))
    val newName: StateFlow<TextFieldValue> = _newName

    private val _newEmail = MutableStateFlow(TextFieldValue(""))
    val newEmail: StateFlow<TextFieldValue> = _newEmail

    private val _newPhone = MutableStateFlow(TextFieldValue(""))
    val newPhone: StateFlow<TextFieldValue> = _newPhone

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState: StateFlow<ErrorState> = _errorState

    private val _saveEvent = MutableSharedFlow<SaveChangesEvents>()
    val saveEvent = _saveEvent.asSharedFlow()

    private val _uploadState = MutableStateFlow<UploadResult<String>?>(null)
    val uploadState: StateFlow<UploadResult<String>?> = _uploadState.asStateFlow()

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

    fun onSaveClick() = viewModelScope.launch {
        val user = currentUser.value ?: return@launch

        val result = updateUserUseCase(
            userId = user.id,
            newName = _newName.value.text.trim(),
            newEmail = _newEmail.value.text.trim(),
            newPhone = _newPhone.value.text.trim()
        )

        when (result) {
            is UserUpdateResult.Success -> {
                _saveEvent.emit(SaveChangesEvents.NavigateToProfile)
            }

            is UserUpdateResult.Failure -> {
                val fieldError = UpdateUserErrorMapper.toFieldError(result.error)

                when (fieldError.field) {
                    FieldType.NAME -> _errorState.value = _errorState.value.copy(nameError = true, errorMsg = fieldError.message)
                    FieldType.EMAIL -> _errorState.value = _errorState.value.copy(emailError = true, errorMsg = fieldError.message)
                    FieldType.PHONE -> _errorState.value = _errorState.value.copy(numberError = true, errorMsg = fieldError.message)
                    FieldType.NONE -> _saveEvent.emit(SaveChangesEvents.ShowSnackbar(fieldError.message))
                    else -> _errorState.value = _errorState.value.copy(errorMsg = fieldError.message)
                }
            }
        }
    }

    fun onImageSelected(uri: Uri) = viewModelScope.launch {
        _uploadState.value = null
        val result = uploadProfilePictureUseCase(currentUser.value?.id ?: return@launch, uri)

        _uploadState.value = when (result) {
            is UserUpdateResult.Success -> {
                _saveEvent.emit(SaveChangesEvents.ShowSnackbar("Profile picture updated"))
                UploadResult.Success(result.user.profilePictureUrl ?: "")
            }

            is UserUpdateResult.Failure -> {
                _saveEvent.emit(SaveChangesEvents.ShowSnackbar("Profile picture upload failed!"))
                UploadResult.Failure(Exception("Upload failed"))
            }
        }
    }
}
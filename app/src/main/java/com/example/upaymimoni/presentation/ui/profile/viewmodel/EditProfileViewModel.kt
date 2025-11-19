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
import com.example.upaymimoni.presentation.ui.profile.viewmodel.SaveChangesEvents.*
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

    private val _pendingImageUri = MutableStateFlow<Uri?>(null)
    val pendingImageUri: StateFlow<Uri?> = _pendingImageUri

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _uploadProgress = MutableStateFlow<Double?>(null)
    val uploadProgress: StateFlow<Double?> = _uploadProgress.asStateFlow()

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

        _loading.value = true
        _errorState.value = ErrorState()

        val uploadImageUrl = uploadImage(user)

        _uploadProgress.value = null

        val result = updateUserUseCase(
            userId = user.id,
            newName = _newName.value.text.trim(),
            newEmail = _newEmail.value.text.trim(),
            newPhone = _newPhone.value.text.trim(),
            newImageUrl = uploadImageUrl
        )

        when (result) {
            is UserUpdateResult.Success -> {
                _saveEvent.emit(SaveChangesEvents.NavigateToProfile)
            }

            is UserUpdateResult.Failure -> {
                val fieldError = UpdateUserErrorMapper.toFieldError(result.error)

                when (fieldError.field) {
                    FieldType.NAME -> _errorState.value =
                        _errorState.value.copy(nameError = true, errorMsg = fieldError.message)

                    FieldType.EMAIL -> _errorState.value =
                        _errorState.value.copy(emailError = true, errorMsg = fieldError.message)

                    FieldType.PHONE -> _errorState.value =
                        _errorState.value.copy(numberError = true, errorMsg = fieldError.message)

                    FieldType.NONE -> _saveEvent.emit(SaveChangesEvents.ShowSnackbar(fieldError.message))
                    else -> _errorState.value =
                        _errorState.value.copy(errorMsg = fieldError.message)
                }
            }
        }

        _loading.value = false
    }

    fun onImageSelected(uri: Uri) {
        _pendingImageUri.value = uri
    }

    private suspend fun uploadImage(user: User): String? {
        val uri = _pendingImageUri.value ?: return null
        val result = uploadProfilePictureUseCase(user.id, uri) { progress ->
            _uploadProgress.value = progress / 100.0
        }

        return when (result) {
            is UploadResult.Success -> result.data
            is UploadResult.Failure -> {
                _saveEvent.emit(ShowSnackbar("Failed to upload profile picture"))
                null
            }
        }
    }
}
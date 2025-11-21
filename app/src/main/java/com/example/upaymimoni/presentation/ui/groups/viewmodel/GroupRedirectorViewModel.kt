package com.example.upaymimoni.presentation.ui.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GroupUiState {
    data object Loading : GroupUiState()
    data class Success(val group: Group) : GroupUiState()
    data class Failure(val message: String) : GroupUiState()
}

class GroupRedirectorViewModel(
    private val getGroupUseCase: GetGroupUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<GroupUiState>(GroupUiState.Loading)
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    fun loadGroup(groupId: String) = viewModelScope.launch {
        _uiState.value = GroupUiState.Loading
        val result = getGroupUseCase(groupId)

        result.fold(
            onSuccess = {
                _uiState.value = GroupUiState.Success(it)
            },
            onFailure = {
                _uiState.value = GroupUiState.Failure(
                    it.message
                        ?: "Failed to get group information, please ensure you have a valid connection."
                )
            }
        )
    }
}
package com.example.upaymimoni.presentation.ui.groups

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.domain.usecase.groups.UpdateGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GroupEditState(
    val group: Group? = null,

    val editableGroupName: String = "",
    var editableMembers: List<String> = emptyList(),

    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)

class EditGroupViewModel(
    private val getGroupUseCase: GetGroupUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase,
    private val groupId: String
) : ViewModel() {

    private val _state = MutableStateFlow(GroupEditState())
    val state: StateFlow<GroupEditState> = _state.asStateFlow()

    init {
        // Start collecting expenses when the ViewModel is created
        loadGroupDetail()
    }

    //Basic group state and manipulation
    fun onNameChange(newName: String) {
        _state.update { it.copy(editableGroupName = newName) }
    }

    fun onMemberAdd(newMember: String) {
        // just to make sure no non user and isnt already in group
        if (newMember.isNotBlank() && !_state.value.editableMembers.contains(newMember)) {
            _state.update {
                it.copy(editableMembers = it.editableMembers + newMember.trim())
            }
        }
    }

    fun onMemberRemove(member: String) {
        _state.update {
            it.copy(editableMembers = it.editableMembers - member)
        }
    }


    /**
     * function for loading single expense
     */
    private fun loadGroupDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getGroupUseCase(groupId)

            result.onSuccess { group ->
                _state.update {
                    it.copy(
                        group = group,
                        editableGroupName = group.groupName,
                        editableMembers = group.members!!,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                Log.e("GroupDetailViewModel", "Failed to load group $groupId", e)
                _state.update {
                    it.copy(isLoading = false, error = "Failed to load group: ${e.message}")
                }
            }

        }
    }

    fun confirmChanges(onSuccess: () -> Unit) {
        val current = state.value
        val group = current.group

        //make sure group is something and name is not blank
        if (group == null || current.editableGroupName.isBlank()) {
            _state.update { it.copy(error = "Group name cannot be empty.") }
            return
        }

        //transfer changes to object
        val updatedGroup = group.copy(
            groupName = current.editableGroupName,
            members = current.editableMembers
        )

        //send to repo
        viewModelScope.launch{
            _state.update { it.copy(isSaving = true, error = null) }

            try {
                //change group use case
                updateGroupUseCase(updatedGroup)

                _state.update { it.copy(group = updatedGroup,isSaving = false, error = null) }
                onSuccess()
            }catch (e: Exception) {
                _state.update { it.copy(isSaving = false,
                    error = "Failed to change group: ${e.message}")
                }

            }

        }
    }



}
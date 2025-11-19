package com.example.upaymimoni.presentation.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.example.upaymimoni.domain.model.Group
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupScreen(
    groupId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: EditGroupViewModel = koinViewModel { parametersOf(groupId) },
) {
    // Collect the state containing the detailed expense data
    val state by viewModel.state.collectAsState()

    EditGroupContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = {
            viewModel.confirmChanges {
                onBackClick()
            }
        },
        onNameChange = viewModel::onNameChange,
        onMemberAdd = viewModel::onMemberAdd,
        onMemberRemove = viewModel::onMemberRemove
    ) 
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupContent(
    state: GroupEditState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onMemberAdd: (String) -> Unit,
    onMemberRemove: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.group?.groupName?: "Edit Group") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary,
                    actionIconContentColor = MaterialTheme.colorScheme.onTertiary
                ), //TODO need maybe check for saving and show indicator
                actions = {
                    IconButton(onClick = onSaveClick) {
                        Icon(Icons.Filled.Save,
                            contentDescription = "Save",
                            modifier = Modifier.size(32.dp))
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> LoadingIndicator()
                state.group != null -> EditGroupBody(
                    state,
                    onBackClick = onBackClick,
                    onSaveClick = onSaveClick,
                    onNameChange = onNameChange,
                    onMemberAdd = onMemberAdd,
                    onMemberRemove = onMemberRemove,
                )
                else -> NoDataMessage()
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Loading Group...", style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupBody(
    state: GroupEditState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onMemberAdd: (String) -> Unit,
    onMemberRemove: (String) -> Unit
) {
    val group = state.group ?: return

    Spacer(modifier = Modifier.height(20.dp))

    BasicGroupInfo(group, state.editableGroupName, onNameChange, state.error)

    Spacer(modifier = Modifier.height(20.dp))

    MembersCard(
        members = state.editableMembers,
        onMemberAdd = onMemberAdd,
        onMemberRemove = onMemberRemove
    )

    Spacer(modifier = Modifier.height(20.dp))

    MetadataCard(group = group)

}


@Composable
fun BasicGroupInfo(
    group: Group,
    editableName: String,
    onNameChange: (String) -> Unit,
    error: String?
) {
    //Group image, needs show image and changable TODO
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { /* TODO: Change photo */ },
        contentAlignment = Alignment.Center,
    ) {
        Text("Change Photo", textAlign = TextAlign.Center)
    }

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = editableName,
        onValueChange = onNameChange,
        label = { Text("Group Name") },
        modifier = Modifier.fillMaxWidth(),
        isError = error != null && editableName.isBlank(),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun MembersCard(
    members: List<String>,
    onMemberAdd: (String) -> Unit,
    onMemberRemove: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Group Members (${members.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(Modifier.height(8.dp))

            EditableMembersList(
                members = members,
                onMemberAdd = onMemberAdd,
                onMemberRemove = onMemberRemove
            )
        }
    }
}

@Composable
fun EditableMembersList(
    members: List<String>,
    onMemberAdd: (String) -> Unit,
    onMemberRemove: (String) -> Unit
) {
    var newMemberName by remember { mutableStateOf("") }

    // Input field for adding new members TODO needs to verify and add with email
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = newMemberName,
            onValueChange = { newMemberName = it },
            label = { Text("Enter Member Name") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        FilledIconButton(
            onClick = {
                onMemberAdd(newMemberName)
                newMemberName = ""
            },
            enabled = newMemberName.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Member", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }

    // List members TODO need to actually pull the users from id
    Column(modifier = Modifier.fillMaxWidth()) {
        members.forEach { member ->
            MemberEditRow(member = member, onRemove = onMemberRemove)
        }
    }
}

@Composable
fun MemberEditRow(member: String, onRemove: (String) -> Unit) {
    //TODO need to show images and style better
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = member, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        IconButton(onClick = { onRemove(member) }) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove $member",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
    HorizontalDivider(
        Modifier,
        DividerDefaults.Thickness,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun MetadataCard(group: Group) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Group Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(Modifier.height(8.dp))

            //TODO add maube created at detailrow
            DetailRow(label = "Group ID", value = group.id, icon = Icons.Default.Key)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp).padding(end = 8.dp)
        )
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
    }
}



@Composable
fun NoDataMessage() {
    Text(
        text = "Group not found or is missing data.",
        color = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GroupSettingsScreenPreview() {


    UPayMiMoniTheme (darkTheme = false) {
        EditGroupContent(
            onBackClick = {},
            onSaveClick = {},
            state = GroupEditState(
                isSaving = false,
                isLoading = false,
                error = null,
                group = Group(
                    groupName = "test group",
                    members = listOf("Victor","VictorKp")
                )
            ),
            onNameChange = {},
            onMemberAdd = {  },
            onMemberRemove = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GroupSettingsScreenPreviewDark() {


    UPayMiMoniTheme (darkTheme = true) {
        EditGroupContent(
            onBackClick = {},
            onSaveClick = {},
            state = GroupEditState(
                isSaving = false,
                isLoading = false,
                error = null,
                group = Group(
                    groupName = "test group",
                    members = listOf("Victor","VictorKp")
                )
            ),
            onNameChange = {},
            onMemberAdd = {  },
            onMemberRemove = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GroupSettingsScreenPreviewErrorNoGroup() {


    UPayMiMoniTheme (darkTheme = false) {
        EditGroupContent(
            onBackClick = {},
            onSaveClick = {},
            state = GroupEditState(
                isSaving = false,
                isLoading = false,
                error = "Failed to load group",
                group = null
            ),
            onNameChange = {},
            onMemberAdd = {  },
            onMemberRemove = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GroupSettingsScreenPreviewLoading() {


    UPayMiMoniTheme (darkTheme = false) {
        EditGroupContent(
            onBackClick = {},
            onSaveClick = {},
            state = GroupEditState(
                isSaving = false,
                isLoading = true,
                error = null,
                group = Group(
                    groupName = "test group",
                    members = listOf("Victor","VictorKp")
                )
            ),
            onNameChange = {},
            onMemberAdd = {  },
            onMemberRemove = {},
        )
    }
}




package com.example.upaymimoni.presentation.ui.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.presentation.ui.profile.components.CircularProfileImage
import com.example.upaymimoni.presentation.ui.common.components.EditProfileField
import com.example.upaymimoni.presentation.ui.profile.viewmodel.EditProfileViewModel
import com.example.upaymimoni.presentation.ui.profile.viewmodel.ErrorState
import com.example.upaymimoni.presentation.ui.profile.viewmodel.SaveChangesEvents
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreen(
    editViewModel: EditProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val user by editViewModel.currentUser.collectAsState()

    val name by editViewModel.newName.collectAsState()
    val email by editViewModel.newEmail.collectAsState()
    val phone by editViewModel.newPhone.collectAsState()
    val error by editViewModel.errorState.collectAsState()

    val saveEvent = editViewModel.saveEvent


    user?.let {
        LaunchedEffect(user) {
            editViewModel.initializeUser(it)

            saveEvent.collect { event ->
                when (event) {
                    is SaveChangesEvents.NavigateToProfile -> {
                        onBackClick()
                    }
                }
            }
        }

        EditProfileContent(
            currentUser = it,
            name = name,
            updateName = editViewModel::updateName,
            email = email,
            updateEmail = editViewModel::updateEmail,
            phone = phone,
            updatePhone = editViewModel::updatePhone,
            onBackClick = onBackClick,
            onSaveClick = editViewModel::onSaveClick,
            errorState = error
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    currentUser: User,
    name: TextFieldValue,
    updateName: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    updateEmail: (TextFieldValue) -> Unit,
    phone: TextFieldValue,
    updatePhone: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    errorState: ErrorState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 48.dp, horizontal = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProfileImage(
                imageUrl = currentUser.profilePictureUrl,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(1f),
                showUploadButton = true,
                onUploadClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                EditProfileField(
                    value = name,
                    label = "Name",
                    placeHolder = "Enter your name",
                    isError = errorState.nameError,
                    onValueChange = { updateName(it) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Name Icon"
                        )
                    },
                    onFocusLost = {
                        updateName(name.copy(text = name.text.trim()))
                    }
                )

                EditProfileField(
                    value = email,
                    label = "Email",
                    placeHolder = "Enter your email",
                    isError = errorState.emailError,
                    onValueChange = { updateEmail(it) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    }
                )

                EditProfileField(
                    value = phone,
                    label = "Mobile",
                    placeHolder = "Enter your phone number",
                    isError = errorState.numberError,
                    onValueChange = { updatePhone(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Icon"
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = errorState.errorMsg ?: " ",
                color = if (errorState.nameError || errorState.emailError || errorState.numberError) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Transparent
                },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    .defaultMinSize(minHeight = 24.dp)
                    .wrapContentHeight(align = Alignment.Top)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            ElevatedButton(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Save Changes"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfilePreview() {
    UPayMiMoniTheme {
        EditProfileContent(
            currentUser = User(
                id = "abcdef123456",
                profilePictureUrl = "https://api.dicebear.com/9.x/bottts-neutral/svg?seed=testUser",
                displayName = "Not a bot",
                phoneNumber = "88888888",
                email = "notabot@botters.com",
                groups = emptyList()
            ),
            name = TextFieldValue(""),
            updateName = {},
            email = TextFieldValue(""),
            updateEmail = {},
            phone = TextFieldValue(""),
            updatePhone = {},
            onBackClick = {},
            onSaveClick = {},
            errorState = ErrorState(
                emailError = true,
                errorMsg = "Invalid Email"
            )
        )
    }
}
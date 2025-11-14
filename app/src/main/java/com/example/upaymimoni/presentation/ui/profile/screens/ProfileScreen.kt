package com.example.upaymimoni.presentation.ui.profile.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.presentation.ui.common.components.AppBottomNavBar
import com.example.upaymimoni.presentation.ui.profile.components.CircularProfileImage
import com.example.upaymimoni.presentation.ui.profile.viewmodel.LogoutEvents
import com.example.upaymimoni.presentation.ui.profile.viewmodel.ProfileViewModel
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNavigateToGroups: () -> Unit,
) {
    val user by profileViewModel.currentUser.collectAsState()

    val uiEvent = profileViewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is LogoutEvents.NavigateToLogin -> {
                    onNavigateToLogin()
                }
            }
        }
    }

    user?.let {
        ProfileScreenContent(
            currentUser = it,
            onEditProfileClick = onEditProfileClick,
            onNavigateToGroups = onNavigateToGroups,
            onLogoutClick = profileViewModel::onLogoutClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    currentUser: User,
    onEditProfileClick: () -> Unit,
    onNavigateToGroups: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineMedium

                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                ),
                actions = {
                    IconButton(
                        onClick = onEditProfileClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Profile Icon",
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            AppBottomNavBar(
                onGroupsClick = onNavigateToGroups,
                isProfileSelected = true
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
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentUser.displayName ?: "",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = currentUser.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = currentUser.phoneNumber ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedButton(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(212, 36, 36)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout of Profile Icon"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Logout"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    UPayMiMoniTheme {
        ProfileScreenContent(
            currentUser = User(
                id = "abcdef123456",
                profilePictureUrl = "https://api.dicebear.com/9.x/bottts-neutral/svg?seed=testUser",
                displayName = "Not a bot",
                phoneNumber = "88888888",
                email = "notabot@botters.com",
                groups = emptyList()
            ),
            onNavigateToGroups = {},
            onEditProfileClick = {},
            onLogoutClick = {}
        )
    }
}
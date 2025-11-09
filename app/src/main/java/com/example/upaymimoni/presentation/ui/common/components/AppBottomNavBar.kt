package com.example.upaymimoni.presentation.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun AppBottomNavBar(
    // Yes this is a stupid way to do it.
    // But we only require the two fields so lets not overcomplicate it just yet.
    onGroupsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    isProfileSelected: Boolean,
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = "Go To Groups Icon"
                )
            },
            label = { Text("Groups") },
            selected = !isProfileSelected,
            onClick = onGroupsClick
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Go To Profile Icon"
                )
            },
            label = { Text("Profile") },
            selected = isProfileSelected,
            onClick = onProfileClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NavBarPreview() {
    UPayMiMoniTheme {
        Scaffold(
            bottomBar = { AppBottomNavBar(
                onGroupsClick = {},
                onProfileClick = {},
                isProfileSelected = true
            ) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            )
        }
    }
}
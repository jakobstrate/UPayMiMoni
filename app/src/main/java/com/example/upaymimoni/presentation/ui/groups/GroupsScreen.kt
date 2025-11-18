package com.example.upaymimoni.presentation.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.presentation.ui.common.components.AppBottomNavBar
import com.example.upaymimoni.presentation.ui.groups.components.ExpenseItem
import com.example.upaymimoni.presentation.ui.groups.components.GroupItem
import com.example.upaymimoni.presentation.ui.groups.components.InviteButton


@Composable
fun GroupsScreen(
    onNavigateToGroups: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {

    val groups = listOf(
        Group("1", "Sushi night", "", listOf("Jakob", "Mads")),
        Group("2", "Gambling night", "", listOf("Viktor1", "Viktor2")),
    )

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                onGroupsClick = onNavigateToGroups,
                onProfileClick = onNavigateToProfile,
                isProfileSelected = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(paddingValues)
        ) {
            // --- HEADER ---
            Row() {
                Text(
                    text = "Groups",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp).weight(1f)
                )
                InviteButton(Modifier.padding(0.dp,8.dp,16.dp,0.dp))
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(groups) { group ->
                    GroupItem(group)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGroupsScreen() {
    GroupsScreen()
}

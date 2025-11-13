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
import com.example.upaymimoni.presentation.ui.common.components.AppBottomNavBar
import com.example.upaymimoni.presentation.ui.groups.components.ExpenseItem

data class Expense(
    val name: String,
    val description: String,
    val amount: Double,
    val date: String
)

@Composable
fun GroupScreen(
    onNavigateToGroups: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {

    val expenses = listOf(
        Expense("Alice", "Dinner at Sushi Bar", 28.50, "Nov 12"),
        Expense("You", "Groceries", 43.20, "Nov 10"),
        Expense("Bob", "Gas money", 15.00, "Nov 9"),
        Expense("You", "Movie tickets", 22.00, "Nov 7")
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
            Text(
                text = "Group 1",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = "Shared expenses and payments",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            // Current balance
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Your Balance", color = Color.Gray)
//                    Text(
//                        text = "−$15.30",
//                        fontSize = 32.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFD32F2F)
//                    )
//                    Text(
//                        "You owe Alice for dinner",
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//                }
//            }

            //ExpenseList
            Text(
                text = "Recent Activity",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(expenses) { expense ->
                    ExpenseItem(expense = expense)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewGroupScreen() {
    GroupsScreen()
}

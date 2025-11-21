package com.example.upaymimoni.presentation.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.presentation.ui.common.components.AppBottomNavBar
import com.example.upaymimoni.presentation.ui.groups.components.ExpenseItem
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.groups.components.BannerInformation
import com.example.upaymimoni.presentation.ui.groups.components.FloatingCard
import com.example.upaymimoni.presentation.ui.groups.components.SettledGroupPersonsCard
import com.example.upaymimoni.presentation.ui.groups.components.SettledTitleCard
import com.example.upaymimoni.presentation.ui.groups.components.toBannerInformation
import com.example.upaymimoni.presentation.ui.groups.viewmodel.GroupRedirectorViewModel
import com.example.upaymimoni.presentation.ui.groups.viewmodel.GroupUiState
import com.example.upaymimoni.presentation.ui.groups.viewmodel.SettledGroupViewModel
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupScreenRedirector(
    groupId: String,
    viewModel: GroupRedirectorViewModel = koinViewModel(),
    onNavigateToGroups: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToEditGroup: () -> Unit = {},
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.loadGroup(groupId)
    }

    when (val state = uiState) {
        is GroupUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is GroupUiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${state.message}")
                    Button(onClick = { viewModel.loadGroup(groupId) }) {
                        Text("Retry")
                    }
                }
            }
        }

        is GroupUiState.Success -> {
            val group = state.group
            if (group.settled) {
                SettledGroupScreen(
                    group = group,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateBack = onNavigateBack
                )
            } else {
                GroupScreen(
                    onNavigateToGroups = onNavigateToGroups,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToEditGroup = onNavigateToEditGroup
                )
            }
        }
    }
}

@Composable
fun GroupScreen(
    onNavigateToGroups: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToEditGroup: () -> Unit = {},
) {

    val expenses = listOf(
        Expense(
            "Alice",
            "Dinner at Sushi Bar",
            28.50,
            "1",
            "1",
            listOf("1", "2"),
            "what",
            "what",
            System.currentTimeMillis()
        ),
        Expense("You", "Groceries", 43.20, "1", "Nov 10"),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { var menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }

                var menuExpanded = false
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Group") },
                        onClick = {
                            menuExpanded = false
                            onNavigateToEditGroup()
                        }
                    )
                }
            }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettledGroupScreen(
    settledGroupViewModel: SettledGroupViewModel = koinViewModel(),
    group: Group,
    onNavigateToProfile: () -> Unit = {},
    onNavigateBack: () -> Unit
) {

    val personalBalance by settledGroupViewModel.personalBalance.collectAsState()
    val totalExpenditure by settledGroupViewModel.totalExpenditure.collectAsState()
    val debtorList by settledGroupViewModel.debtorList.collectAsState()
    val creditorList by settledGroupViewModel.creditorList.collectAsState()

    LaunchedEffect(group) {
        settledGroupViewModel.populateSettlement(group)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settlement",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                ),
            )
        },
        bottomBar = {
            AppBottomNavBar(
                onProfileClick = onNavigateToProfile,
                isProfileSelected = false
            )
        }
    ) { paddingValues ->
        SettledGroupContent(
            modifier = Modifier.padding(paddingValues),
            balance = personalBalance,
            totalExpenditure = totalExpenditure,
            currency = "DKK",
            debtorList = debtorList,
            creditorList = creditorList,
            onNotifyClick = settledGroupViewModel::onNotifyClick
        )
    }
}

@Composable
fun SettledGroupContent(
    modifier: Modifier = Modifier,
    balance: Double,
    totalExpenditure: Double,
    currency: String,
    debtorList: List<BannerInformation>,
    creditorList: List<BannerInformation>,
    onNotifyClick: (String, Double, String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        SettledTitleCard(
            balance = balance,
            totalExpenditure = totalExpenditure,
            currency = currency
        )

        if (!debtorList.isEmpty()) {
            SettledGroupPersonsCard(
                label = "They owe you",
                banners = debtorList,
                currencyString = currency,
                showNotifyButton = true,
                onNotifyClick = onNotifyClick
            )
        }

        if (!creditorList.isEmpty()) {
            SettledGroupPersonsCard(
                label = "You owe them",
                banners = creditorList,
                currencyString = currency,
                showNotifyButton = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettledGroupScreenPreview() {
    UPayMiMoniTheme {
        SettledGroupContent(
            balance = 120.5,
            totalExpenditure = 755.45,
            currency = "DKK",
            debtorList = listOf(),
            creditorList = listOf(),
            onNotifyClick = { string, d, string1 -> }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGroupScreen() {
    GroupScreen()
}

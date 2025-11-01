package com.example.upaymimoni.presentation.ui

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.data.model.Expense
import com.example.upaymimoni.presentation.viewmodel.AddExpenseState
import com.example.upaymimoni.presentation.viewmodel.ExpenseAddViewModel
import com.example.upaymimoni.presentation.viewmodel.ExpenseDetailViewModel
import java.util.Locale.getDefault
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Screen to display the detailed information for a single expense.
 * It fetches the expense data using the provided expenseId.
 */
@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    onBackClick: () -> Unit,
    viewModel: ExpenseDetailViewModel = koinViewModel { parametersOf(expenseId) }
) {
    // Collect the state containing the detailed expense data
    val state by viewModel.state.collectAsState()

    ExpenseDetailContent(
        state = state,
        onBackClick = onBackClick
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailContent(
    state: Expense?,
    onBackClick: () -> Unit,
) {



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "EditExpense",
                            tint = Color.White
                        )
                    }
                },

            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                state != null -> ExpenseDetailCard(state!!)
                else -> NoDataMessage()
            }
        }
    }
}

// --- Lo-Fi Detail Card (The core of the screen) ---
@Composable
fun ExpenseDetailCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium // Simple rectangular shape
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // 1. Header: Name and Amount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = expense.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "$${String.format("%.2f", expense.amount)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFD32F2F) // Red for expense amount
                )
            }
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // 2. Metadata: Date, Group, Payer
            DetailRow(
                icon = Icons.Default.AccountBox,
                label = "Group ID",
                value = expense.groupId,
                tint = Color(0xFF6A1B9A)
            )
            DetailRow(
                icon = Icons.Default.Person,
                label = "Paid By (User ID)",
                value = expense.payerUserId,
                tint = Color(0xFF00796B)
            )
            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Date",
                value = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", getDefault()).format(expense.createdAt),
                tint = Color(0xFF303F9F)
            )

            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            // 3. Attachment Status
            val hasAttachment = expense.attachment != null
            DetailRow(
                icon = if (hasAttachment) Icons.Default.CheckCircle else Icons.Default.Email,
                label = "Receipt/Invoice",
                value = if (hasAttachment) "Attached (Tap to View)" else "No Attachment",
                tint = if (hasAttachment) Color(0xFF388E3C) else Color(0xFF757575)
            )
            if (hasAttachment) {
                Spacer(Modifier.height(8.dp))
                Button(onClick = { /* TODO: Implement PDF Viewer/Link Opener */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("View Attachment (Simulated)")
                }
            }
        }
    }
}

// Helper Composable for each detail line
@Composable
fun DetailRow(icon: ImageVector, label: String, value: String, tint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF757575) // Lo-fi gray label
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun NoDataMessage() {
    Text(
        text = "Expense not found or is missing data.",
        color = Color(0xFF757575)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewExpenseDetailContent() {
    ExpenseDetailContent(
        state = Expense(
            name = "Coffee",
            amount = 4.20,
            id = "1",
            payerUserId = "2",
            groupId = "3",
            attachment = null,
        ),
        onBackClick = {}
    )
}

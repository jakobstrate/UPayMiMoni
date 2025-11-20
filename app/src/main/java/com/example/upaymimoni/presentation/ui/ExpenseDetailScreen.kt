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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.presentation.ui.expenses.popups.PaidByPopup
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.AddExpenseState
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseDetailState
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseDetailViewModel
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
    viewModel: ExpenseDetailViewModel = koinViewModel { parametersOf(expenseId) },
) {
    // Collect the state containing the detailed expense data
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val showDeleteConfirmation by viewModel.showDeleteConfirmation.collectAsState()

    ExpenseDetailContent(
        state = state,
        onBackClick = onBackClick,
        onViewAttachmentClick = {
            viewModel.openAttachmentUrl(context, state.expense?.attachmentUrl!!)
        },
        onDeleteExpenseClick = viewModel::openShowDeleteConfirmation
    )

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.closeShowDeleteConfirmation() },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to permanently delete this expense? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteExpense(onBackClick)
                        viewModel.closeShowDeleteConfirmation()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.closeShowDeleteConfirmation() }) {
                    Text("Cancel")
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailContent(
    state: ExpenseDetailState,
    onBackClick: () -> Unit,
    onViewAttachmentClick: () -> Unit,
    onDeleteExpenseClick: () -> Unit
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
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                ),
                actions = {
                    if (state.expense != null && !state.isDeleting) {
                        IconButton(onClick = onDeleteExpenseClick) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Expense",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },

            )
        },
        containerColor = MaterialTheme.colorScheme.surface
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
                state.isLoading -> CircularProgressIndicator()
                state.expense != null -> ExpenseDetailCard(
                    state.expense,
                    onViewAttachmentClick = onViewAttachmentClick,
                )
                else -> NoDataMessage()
            }
        }
    }
}

@Composable
fun ExpenseDetailCard(
    expense: Expense,
    onViewAttachmentClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium // Simple rectangular shape
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // Name and Amount
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
                    color = MaterialTheme.colorScheme.error
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)
            )

            // Date, Group, Payer
            DetailRow(
                icon = Icons.Filled.Groups,
                label = "Group ID",
                value = expense.groupId,
                tint = Color(0xFF6A1B9A)
            )
            DetailRow(
                icon = Icons.Filled.Person,
                label = "Paid By (User ID)",
                value = expense.payerUserId,
                tint = MaterialTheme.colorScheme.tertiary
            )
            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Date",
                value = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", getDefault()).format(expense.createdAt),
                tint = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)
            )

            // Attachment Status
            val hasAttachment = expense.attachmentUrl != null
            DetailRow(
                icon = if (hasAttachment) Icons.Default.CheckCircle else Icons.Default.AttachFile,
                label = "Receipt/Invoice",
                value = if (hasAttachment) "Attached (Tap to View)" else "No Attachment",
                tint = MaterialTheme.colorScheme.secondary
            )
            if (hasAttachment) {
                Spacer(Modifier.height(8.dp))
                Button(onClick = onViewAttachmentClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("View Attachment")
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
                .size(32.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NoDataMessage() {
    Text(
        text = "Expense not found or is missing data.",
        color = MaterialTheme.colorScheme.error
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewExpenseDetailContent() {
    val expense = Expense(
        name = "Coffee",
        amount = 4.20,
        id = "1",
        payerUserId = "2",
        groupId = "3",
        attachmentUrl = null,
    )
    UPayMiMoniTheme(darkTheme = false) {
        ExpenseDetailContent(
            state = ExpenseDetailState(
                expense = expense,
                isLoading = false,
                isDeleting = false,
                error = null
            ),
            onBackClick = {},
            onViewAttachmentClick = {},
            onDeleteExpenseClick = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseDetailContentHasAttachment() {
    val expense = Expense(
        name = "Coffee",
        amount = 4.20,
        id = "1",
        payerUserId = "2",
        groupId = "3",
        attachmentUrl = "test.com",
    )
    UPayMiMoniTheme(darkTheme = false) {
        ExpenseDetailContent(
            state = ExpenseDetailState(
                expense = expense,
                isLoading = false,
                isDeleting = false,
                error = null
            ),
            onBackClick = {},
            onViewAttachmentClick = {},
            onDeleteExpenseClick = {  }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExpenseDetailContentHasAttachmentIsDeleting() {
    val expense = Expense(
        name = "Coffee",
        amount = 4.20,
        id = "1",
        payerUserId = "2",
        groupId = "3",
        attachmentUrl = "test.com",
    )
    UPayMiMoniTheme(darkTheme = false) {
        ExpenseDetailContent(
            state = ExpenseDetailState(
                expense = expense,
                isLoading = false,
                isDeleting = true,
                error = null
            ),
            onBackClick = {},
            onViewAttachmentClick = {},
            onDeleteExpenseClick = { }
        )
    }
}


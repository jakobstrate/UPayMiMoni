package com.example.upaymimoni.presentation.ui

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.presentation.viewmodel.AddExpenseState
import com.example.upaymimoni.presentation.viewmodel.ExpenseAddViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun ExpenseAddScreen(
    groupId: String,
    userId: String,
    onBackClick: () -> Unit,
    onExpenseAdded: () -> Unit,
    viewModel: ExpenseAddViewModel = koinViewModel { parametersOf(groupId, userId) }
) {
    val state by viewModel.state.collectAsState()

    ExpenseAddContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onAmountChange = viewModel::onAmountChange,
        onSaveClick = {
            viewModel.confirmExpense {
                onExpenseAdded()
                onBackClick()
            }
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddContent(
    state: AddExpenseState,
    onNameChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(color = LocalContentColor.current.copy(alpha = 0.5f),
                        text = "Add Expense")
                    Text(
                        fontSize = 16.sp,
                        text = "Group")
                }  },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(0.5f)
                        .padding(bottom = 16.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving)
                        CircularProgressIndicator(Modifier.size(18.dp))
                    else Text("Confirm Expense")
                }
            }
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
            ExpenseAddBody(
                state = state,
                onNameChange = onNameChange,
                onAmountChange = onAmountChange,
                onSaveClick = onSaveClick,
            )
        }
    }

}

@Composable
fun ExpenseAddBody(
    state: AddExpenseState,
    onNameChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {}) {
    Column(Modifier.padding(16.dp)) {

        TextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Title on expense") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))


        TextField(
            value = state.amount,
            onValueChange = onAmountChange,
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )



        state.error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseAddContent() {
    ExpenseAddContent(
        state = AddExpenseState(
            name = "Coffee",
            amount = "4.20",
            isSaving = false,
            error = null
        )
    )
}
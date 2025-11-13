package com.example.upaymimoni.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.presentation.ui.expenses.components.AmountSelector
import com.example.upaymimoni.presentation.ui.expenses.components.ConfirmSlider
import com.example.upaymimoni.presentation.ui.expenses.components.ExpenseNameField
import com.example.upaymimoni.presentation.ui.expenses.components.SelectionBar
import com.example.upaymimoni.presentation.ui.expenses.popups.PaidByPopup
import com.example.upaymimoni.presentation.ui.expenses.popups.SplitBetweenPopup
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.AddExpenseState
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.ExpenseAddViewModel
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

    val showPaidByPopup by viewModel.showPaidByPopup.collectAsState()
    val showSplitBetweenPopup by viewModel.showSplitBetweenPopup.collectAsState()
    //observe temp selected split betweens
    val pendingSelectedUserIds by viewModel.pendingSelectedUserIds.collectAsState()


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
        onBackClick = onBackClick,
        onOpenSplitBetweenPopup = viewModel::openSpltBetweenPopup,
        onOpenPaidByPopup = viewModel::openPaidByPopup,
        onOpenAttachFilePopup = viewModel::openFileAttachmentChooser,
        onSetSliderConfirmed = viewModel::setSliderConfirmed
    )

    // Popups Displayed on top
    //TODO use actual values
    //paidby popup
    if (showPaidByPopup) {
        PaidByPopup(
            options = listOf(
                "Adam",
                "Mack",
                "Nick",
                "Muhammad",
                "nickie",
                "crystal",
                "metha",
                "niels",
                "anders"
            ),
            selected = state.paidByUserId,
            onSelect = { selectedUser ->
                viewModel.updatePaidByUserId(selectedUser) // Update selection
                viewModel.closePaidByPopup() // Closes on selection
            },
            onClose = viewModel::closePaidByPopup
        )
    }

    //split between popup
    if (showSplitBetweenPopup) {
        SplitBetweenPopup(
            options = listOf(
                "Adam",
                "Mack",
                "Nick",
                "Muhammad",
                "nickie",
                "crystal",
                "metha",
                "niels",
                "anders"
            ),
            selected = pendingSelectedUserIds,
            onToggleSelection = viewModel::toggleUserSelection, // update selection
            onClose = viewModel::closeSpltBetweenPopup,
            onConfirm = { confirmedUsers ->
                viewModel.saveConfirmedSplitBetweenUserIds(confirmedUsers) // Save choices
                viewModel.closeSpltBetweenPopup()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddContent(
    state: AddExpenseState,
    onNameChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onOpenSplitBetweenPopup: () -> Unit = {},
    onOpenPaidByPopup: () -> Unit = {},
    onOpenAttachFilePopup: () -> Unit = {},
    onSetSliderConfirmed: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current

    // click action to clear focus
    val clearFocusOnClick = {
        focusManager.clearFocus()
    }

    Scaffold(
        //to clear focus of interaction elements when i click anywhere else on screen.
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            clearFocusOnClick()
        },
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(color = MaterialTheme.colorScheme.onTertiary,
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
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                )
            )
        },
        bottomBar = {
            println("Rerender confirm slider : ${state.isSliderConfirmed}")
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                ConfirmSlider(
                    sliderText = "Confirm",
                    onConfirmed = onSaveClick,
                    isConfirmed = state.isSliderConfirmed,
                    onSetSliderConfirmed = { onSetSliderConfirmed(true) }
                )
            }
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
            ExpenseAddBody(
                state = state,
                onNameChange = onNameChange,
                onAmountChange = onAmountChange,
                onSaveClick = onSaveClick,
                onOpenSplitBetweenPopup = onOpenSplitBetweenPopup,
                onOpenPaidByPopup = onOpenPaidByPopup,
                onOpenAttachFilePopup = onOpenAttachFilePopup
            )
        }
    }

}

@Composable
fun ExpenseAddBody(
    state: AddExpenseState,
    onNameChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onOpenSplitBetweenPopup: () -> Unit,
    onOpenPaidByPopup: () -> Unit,
    onOpenAttachFilePopup: () -> Unit
) {

    val isNameError = state.error != null && state.name.isBlank()
    val isPaidByError = state.error != null && state.paidByUserId.isBlank()
    val isSplitBetweenError = state.error != null && state.splitBetweenUserIds.isEmpty()
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        ExpenseNameField(
            value = state.name,
            onValueChange = onNameChange,
            isError = isNameError
        )

        Spacer(Modifier.height(36.dp))

        AmountSelector(
            value = state.amount,
            onValueChange = onAmountChange,
        )

        Spacer(Modifier.height(36.dp))

        
        SelectionBar(
            "Paid By",
            state.paidByUserId,
            onOpenPaidByPopup,
            isError = isPaidByError
        )

        Spacer(Modifier.height(12.dp))

        SelectionBar(
            "Split Between",
            state.splitBetweenUserIds.joinToString(separator = ", ", prefix = "", postfix = ""),
            onOpenSplitBetweenPopup,
            isError = isSplitBetweenError
        )

        Spacer(Modifier.height(36.dp))

        ElevatedButton(
            onClick = onOpenAttachFilePopup,
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(16.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,

            ),
        ) {
            Text("Attach file")
            Icon(Icons.Filled.Add,contentDescription = "Attach")
        }

        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExpenseAddContent() {
    UPayMiMoniTheme (darkTheme = true) {
        ExpenseAddContent(
            state = AddExpenseState(
                name = "Coffee",
                amount = "4.20",
                isSaving = false,
                isSliderConfirmed = false,
                error = null
            ),
            onSetSliderConfirmed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBasicErrorNoNameExpenseAddContent() {
    UPayMiMoniTheme (darkTheme = true) {
        ExpenseAddContent(
            state = AddExpenseState(
                name = "",
                amount = "4.20",
                isSaving = true,
                isSliderConfirmed = false,
                error = "No expense title",
                paidByUserId = "Adam Azulia",
                splitBetweenUserIds = listOf("Mack, Nick")
            ),
            onSetSliderConfirmed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBasicErrorNoPaidByExpenseAddContent() {
    UPayMiMoniTheme (darkTheme = true) {
        ExpenseAddContent(
            state = AddExpenseState(
                name = "Coffee",
                amount = "4.20",
                isSaving = true,
                isSliderConfirmed = false,
                error = "No paid by and split between user selected",
                paidByUserId = "",
                splitBetweenUserIds = listOf()
            ),
            onSetSliderConfirmed = {}
        )
    }
}
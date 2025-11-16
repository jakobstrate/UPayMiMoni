package com.example.upaymimoni.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.core.net.toUri


/**
 * Helper function to retrieve the actual file name from a Content URI.
 */
private fun getFileName(context: Context, uri: Uri): String {
    var name = "unknown_file.pdf"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use { // just to avoid if not found or other throwns,
        // since it wouldn't matter besides, can i get it or not
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

@Composable
fun ExpenseAddScreen(
    groupId: String,
    userId: String,
    onBackClick: () -> Unit,
    onExpenseAdded: () -> Unit,
    viewModel: ExpenseAddViewModel = koinViewModel { parametersOf(groupId, userId) }
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val showPaidByPopup by viewModel.showPaidByPopup.collectAsState()
    val showSplitBetweenPopup by viewModel.showSplitBetweenPopup.collectAsState()
    //observe temp selected split betweens
    val pendingSelectedUserIds by viewModel.pendingSelectedUserIds.collectAsState()

    //attach file, file choosing
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setAttachmentUri(uri)
        viewModel.updateAttachmentStatus(if (uri != null) {
            "File selected: ${getFileName(context, uri)}"
        } else {
            "File selection cancelled"
        })
    }

    val onPreviewAttachment: () -> Unit = {
        state.attachmentUri?.let { uri ->
            println("Attempting to preview file: ${getFileName(context, uri)} ($uri)")
            try {
                val mimeType = context.contentResolver.getType(uri) ?: "*/*"
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, mimeType)
                    // need to add read access to viewer app
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                // there needs to be an application to handle the intetn,
                // but let user choose instead of looking self, since in sandboxed emulator environment
                context.startActivity(Intent.createChooser(intent, "Open file with..."))
            } catch (e: Exception) {
                Log.e("Previewer", "Could not launch file viewer for $uri", e)
            }
        }
    }

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
        onOpenAttachFilePopup = {
            filePickerLauncher.launch("*/*") //wildcard,
                                // since application/pdf,image wouldn't select images
        },
        onSetSliderConfirmed = viewModel::setSliderConfirmed,
        onPreviewAttachment = onPreviewAttachment,
        onRemoveAttachment = viewModel::removeAttachment,
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
    onSetSliderConfirmed: (Boolean) -> Unit,
    onPreviewAttachment: () -> Unit = {},
    onRemoveAttachment: () -> Unit = {}
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
                onOpenAttachFilePopup = onOpenAttachFilePopup,
                onPreviewAttachment = onPreviewAttachment,
                onRemoveAttachment = onRemoveAttachment
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
    onOpenSplitBetweenPopup: () -> Unit = {},
    onOpenPaidByPopup: () -> Unit = {},
    onOpenAttachFilePopup: () -> Unit = {},
    onPreviewAttachment: () -> Unit = {},
    onRemoveAttachment: () -> Unit = {}
) {

    val isNameError = state.error != null && state.name.isBlank()
    val isPaidByError = state.error != null && state.paidByUserId.isBlank()
    val isSplitBetweenError = state.error != null && state.splitBetweenUserIds.isEmpty()

    val isAttachmentUploading = state.isSaving && state.attachmentUri != null && state.attachmentStatus == "Uploading file"

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

        //attach file button
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (state.attachmentUri != null) "Change Attached File" else "Attach file")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Filled.Add, contentDescription = "Attach")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Display attached file status and upload progress
        AttachmentDisplay(
            state = state,
            onRemoveAttachment = onRemoveAttachment,
            onPreviewAttachment = onPreviewAttachment
        )

        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun AttachmentDisplay(
    state: AddExpenseState,
    onRemoveAttachment: () -> Unit,
    onPreviewAttachment: () -> Unit
) {
    val context = LocalContext.current

    // Determine if the attachment upload is actively in progress
    val isAttachmentUploading =
        state.isSaving && state.attachmentUri != null && state.attachmentStatus == "Uploading file"
    state.attachmentUri?.let { uri ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // file detail row, name, preview and remove
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(0.9f)
            ) {
                val fileName = getFileName(LocalContext.current, uri)

                Text(
                    text = fileName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Preview file button
                    IconButton(
                        onClick = onPreviewAttachment,
                        modifier = Modifier.size(32.dp),
                        enabled = !isAttachmentUploading // don't enable ujntil uploaded
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Preview Attachment",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Remove button
                    IconButton(
                        onClick = onRemoveAttachment,
                        modifier = Modifier.size(32.dp),
                        enabled = !isAttachmentUploading // don't enable until uploaded
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove Attachment",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Attachment status and progress indicator when uploading
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isAttachmentUploading) {
                    // Circular Progress Indicator for active upload
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = state.attachmentStatus,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (state.error != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.tertiary
                )
            }
        }
    } ?: Text( // no file attached
        text = "",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
    )
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
fun PreviewExpenseAddContentAttachmentSelected() {
    UPayMiMoniTheme (darkTheme = true) {
        ExpenseAddContent(
            state = AddExpenseState(
                name = "Coffee",
                amount = "4.20",
                isSaving = false,
                isSliderConfirmed = false,
                error = null,
                attachmentUri = "test".toUri()
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
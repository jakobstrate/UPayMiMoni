package com.example.upaymimoni.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.presentation.viewmodel.AddExpenseState
import com.example.upaymimoni.presentation.viewmodel.ExpenseAddViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt


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
                ConfirmSlider(
                    sliderText = "Confirm",
                    onConfirmed = {  }
                )
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
    var showPaidByPopup by remember { mutableStateOf(false) }
    var showSplitPopup by remember { mutableStateOf(false) }
    var showAttachFilePopup by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Title on expense") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(36.dp))
        
        AmountSelector(
            value = state.amount,
            onValueChange = onAmountChange,
        )

        Spacer(Modifier.height(36.dp))

        SelectionBar("Paid By",
            "Adam",
            {showPaidByPopup=true})

        Spacer(Modifier.height(12.dp))

        SelectionBar("Split Between",
            "Alof, Aduf, Asjuf",
            {showPaidByPopup=true})

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = { showAttachFilePopup = true },
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            Text("Attach file")
            Icon(Icons.Filled.Add,contentDescription = "Attach")
        }

        state.error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}



/**
 * for selecting amount on expense
 */
@Composable
fun AmountSelector(
    value: String,
    onValueChange: (String) -> Unit,
    currency: String = "DKK"
) {
    val focusRequester = remember { FocusRequester() }

    // The "hidden" textfield
    TextField(
        value = value,
        onValueChange = { newValue ->
            // Allow only numbers
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done),
        modifier = Modifier
            .size(1.dp) //small as fuck, so cant be clicked
            .alpha(0f) // invisible
            .focusRequester(focusRequester)
            .onKeyEvent{
                false
            },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Transparent,
            focusedTextColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            cursorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    // What user sees & taps
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                focusRequester.requestFocus()
            }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = (value.ifEmpty { "0" }),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 42.sp,
                color = Color.DarkGray
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = currency,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * TODO
 * Popup for showing and selecting, who paid for expense
 */
@Composable
fun PaidByPopup(label: String, value: String, onClick: () -> Unit) {

}
/**
 * TODO
 * Popup for showing and selecting, who to split expense among
 */
@Composable
fun SplitBetweenPopup(label: String, value: String, onClick: () -> Unit){

}

/**
 * selection bar
 */
@Composable
fun SelectionBar(label: String, value: String, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(value, Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }
}


@Composable
fun ConfirmSlider(
    sliderText: String = "Confirm",
    onConfirmed: () -> Unit
) {
    //sizes and offsets, used for movement and detecting reached end
    var barSize by remember { mutableStateOf(IntSize.Zero) }
    var handleSize by remember { mutableStateOf(IntSize.Zero) }
    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var confirmed by remember { mutableStateOf(false) }

    //Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp)
            .onGloballyPositioned { barSize = it.size }
            .background(Color.LightGray, RoundedCornerShape(50)),
        contentAlignment = Alignment.CenterStart
    ) {
        val maxOffset = (barSize.width - handleSize.width).toFloat()

        // Text in center
        Text(
            text = "Slide to $sliderText",
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.Center)
        )

        // Draggable handle
        Box(
            modifier = Modifier
                .offset {
                    //unlock position if not confirmed, else lock
                    if (!confirmed) {
                        IntOffset(dragOffset.value.roundToInt(), 0)
                    } else {
                        IntOffset(maxOffset.roundToInt(), 0)
                    }
                }
                .size(56.dp)
                .onGloballyPositioned { handleSize = it.size }
                .background(Color(0xFF236DF1), RoundedCornerShape(50))
                .pointerInput(maxOffset) {
                    if (!confirmed) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                println("barSize:$barSize")
                                println("handle:$handleSize")
                                println("dragOffset:${dragOffset.value}")
                                change.consume()
                                scope.launch {
                                    dragOffset.snapTo(
                                        (dragOffset.value + dragAmount.x)
                                            .coerceIn(0f, maxOffset)
                                    )
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val shouldConfirm = dragOffset.value > maxOffset * 0.75f
                                    // Trigger confirm if released at end
                                    if (shouldConfirm) {
                                        confirmed = true
                                        dragOffset.animateTo(maxOffset, tween(180))
                                        onConfirmed()
                                    } else {
                                        dragOffset.animateTo(0f, tween(200))
                                    }
                                }
                            }
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (confirmed) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Icon(Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ConfirmArrow",
                    tint = Color.White)
            }
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
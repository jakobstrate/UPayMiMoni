package com.example.upaymimoni.presentation.ui.expenses.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun ExpenseNameField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Dynamic Elevation
    val targetElevation = if (isFocused)
        2.dp else 0.dp

    val animatedElevation by animateDpAsState(
        targetValue = targetElevation,
        label = "TextFieldElevation"
    )

    val baseShadowColor = MaterialTheme.colorScheme.primary
    val errorShadowColor = MaterialTheme.colorScheme.error

    // shadow colors based on error or not
    val shadowColor = if (isError) errorShadowColor else baseShadowColor

    val containerShape = RoundedCornerShape(50)

    // Wrapper Box
    Surface(
        shadowElevation = 4.dp,
        shape = containerShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            // apply the shadow modifier first
            .shadow(
                elevation = animatedElevation,
                shape = containerShape,
                // shadow colors
                ambientColor = shadowColor.copy(alpha = 0.3f),
                spotColor = shadowColor.copy(alpha = 0.5f)
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    ) {
        OutlinedTextField(
            textStyle = MaterialTheme.typography.titleMedium,
            value = value,
            onValueChange = onValueChange,
            // Pass the error state to the M3 component
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .background(shadowColor.copy(alpha = 0.3f)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,

                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colorScheme.primary,

                // Transparent/no border
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
            ),
            shape = containerShape,
            placeholder = { Text("Enter expense title") },
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseNameField() {
    UPayMiMoniTheme (darkTheme = false) {
        ExpenseNameField(
            value = "Name",
            onValueChange = {},
            isError = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseNameFieldError() {
    UPayMiMoniTheme (darkTheme = false) {
        ExpenseNameField(
            value = "Name",
            onValueChange = {},
            isError = true
        )
    }
}

package com.example.upaymimoni.presentation.ui.expenses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

/**
 * selection bar
 */
@Composable
fun SelectionBar(
    label: String,
    value: String,
    onClick: () -> Unit,
    isError: Boolean
) {
    val barShape = RoundedCornerShape(12.dp)

    // Conditional border color based on error state
    val borderColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.surfaceDim
    }

    // Conditional content color (text and icon)
    val contentColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else if (value.isBlank()) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val displayValue = if (value.isBlank()) "Tap to select..." else value

    Column(Modifier.fillMaxWidth()) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isError) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            shadowElevation = 4.dp,
            shape = barShape,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, borderColor, barShape)
                .clickable(onClick = onClick)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = barShape
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayValue,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    Icons.Default.ArrowDropDown, contentDescription = null,
                    tint = contentColor
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectionBar() {
    UPayMiMoniTheme(darkTheme = false) {
        SelectionBar(
            label = "Label",
            value = "Adam Azulia",
            onClick = {},
            isError = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectionBarError() {
    UPayMiMoniTheme(darkTheme = false) {
        SelectionBar(
            label = "Label",
            value = "",
            onClick = {},
            isError = true
        )
    }
}
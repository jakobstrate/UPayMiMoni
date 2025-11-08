package com.example.upaymimoni.presentation.ui.auth.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthButtonPreviewLoading() {
    UPayMiMoniTheme {
        AuthButton(
            "Test Button",
            enabled = false,
            onClick = { println("Test Button Callback")},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthButtonPreview() {
    UPayMiMoniTheme {
        AuthButton(
            "Test Button",
            enabled = true,
            onClick = { println("Test Button Callback")},
        )
    }
}

package com.example.upaymimoni.presentation.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun UserInputField(
    label: String,
    placeHolder: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )

        OutlinedTextField(
            label = { Text(placeHolder)},
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputFieldPreview() {
    UPayMiMoniTheme {
        UserInputField(
            label = "Test Label",
            placeHolder = "Test Placeholder",
            value = TextFieldValue(),
            onValueChange = { println("Test value has changed") },
        )
    }
}
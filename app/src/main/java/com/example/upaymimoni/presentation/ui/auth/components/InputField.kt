package com.example.upaymimoni.presentation.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun UserInputField(
    value: TextFieldValue,
    label: String,
    placeHolder: String,
    isError: Boolean,
    isPasswordField: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(!isPasswordField) }
    val currentTransformation = if (isPasswordField && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val trailingIcon = if (isPasswordField) {
        @androidx.compose.runtime.Composable {
            val image = if (passwordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(imageVector = image, contentDescription = "Toogle Password Visibility")
            }
        }
    } else {
        null
    }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue -> // Removes whitespace from input before updating state.
                val filteredText = newValue.text.filter { !it.isWhitespace() }
                val filteredValue = newValue.copy(text = filteredText)
                onValueChange(filteredValue)
            },
            label = { Text(label) },
            placeholder = { Text(placeHolder) },
            singleLine = true,
            visualTransformation = currentTransformation,
            isError = isError,
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputFieldPreview() {
    UPayMiMoniTheme {
        UserInputField(
            value = TextFieldValue(),
            label = "Test Label",
            placeHolder = "Test Placeholder",
            isError = true,
            onValueChange = { println("Test value has changed") },
        )
    }
}

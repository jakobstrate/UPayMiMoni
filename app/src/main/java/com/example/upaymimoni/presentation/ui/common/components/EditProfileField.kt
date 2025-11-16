package com.example.upaymimoni.presentation.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun EditProfileField(
    value: TextFieldValue,
    label: String,
    placeHolder: String,
    isError: Boolean,
    trailingIcon: (@Composable (() -> Unit))? = null,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusLost: () -> Unit = {},
    isEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default

) {

    var hasFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        enabled = isEnabled,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        isError = isError,
        trailingIcon = trailingIcon,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged{ state ->
                if (hasFocus && !state.isFocused) {
                    onFocusLost()
                }

                hasFocus = state.isFocused
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun EditProfileFieldPreview() {
    UPayMiMoniTheme {
        EditProfileField(
            value = TextFieldValue(""),
            onValueChange = {},
            label = "Label",
            placeHolder = "Placeholder",
            isError = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Preview,
                    contentDescription = "Preview Icon"
                )
            }
        )
    }
}
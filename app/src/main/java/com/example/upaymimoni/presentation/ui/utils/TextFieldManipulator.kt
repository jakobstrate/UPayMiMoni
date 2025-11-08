package com.example.upaymimoni.presentation.ui.utils

import androidx.compose.ui.text.input.TextFieldValue

object TextFieldManipulator {
    fun clearWhiteSpaceFromField(value: TextFieldValue): TextFieldValue {
        val filteredText = value.text.filter { !it.isWhitespace() }
        return value.copy(text = filteredText)
    }

    fun removeAllNonNumerics(value: TextFieldValue): TextFieldValue {
        val filtered = value.text.filter { it.isDigit() || it == '+' || it == '-' }
        return value.copy(text = filtered)
    }
}
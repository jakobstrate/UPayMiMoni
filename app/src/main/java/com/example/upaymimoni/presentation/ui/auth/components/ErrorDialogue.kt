package com.example.upaymimoni.presentation.ui.auth.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun ErrorDialogue(
    active: Boolean,
    message: String?,
) {
    Text(
        text = message ?: " ",
        color = if (active) MaterialTheme.colorScheme.error else Color.Transparent,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .padding(start = 16.dp, top = 4.dp, end = 16.dp)
            .defaultMinSize(minHeight = 24.dp)
            .wrapContentHeight(align = Alignment.Top)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorDiaPreview() {
    UPayMiMoniTheme {
        ErrorDialogue(
            active = true,
            message = "There was an error"
        )
    }
}
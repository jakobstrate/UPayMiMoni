package com.example.upaymimoni.presentation.ui.expenses.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

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
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .size(1.dp) //small as fuck, so cant be clicked
            .alpha(0f) // invisible
            .focusRequester(focusRequester)
            .onKeyEvent {
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
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = currency,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAmountSelector() {
    UPayMiMoniTheme(darkTheme = false) {
        AmountSelector(
            value = "0",
            onValueChange = {},
        )
    }
}
package com.example.upaymimoni.presentation.ui.common.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.upaymimoni.presentation.ui.common.components.EditProfileField
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun EnsurePhonePopup(
    onConfirmClick: () -> Unit,
    phone: TextFieldValue,
    updatePhone: (TextFieldValue) -> Unit,
    isError: Boolean,
    errorMsg: String?
) {
    Dialog(
        onDismissRequest = {}
    ) {
        EnsurePhonePopupContent(
            onConfirmClick = onConfirmClick,
            phone = phone,
            updatePhone = updatePhone,
            isError = isError,
            errorMsg = errorMsg
        )
    }
}

@Composable
fun EnsurePhonePopupContent(
    onConfirmClick: () -> Unit,
    phone: TextFieldValue,
    updatePhone: (TextFieldValue) -> Unit,
    isError: Boolean,
    errorMsg: String?
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 24.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = "Mobile Phone Icon",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Enter your mobile number",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "We need your mobile number to complete your profile. Please enter it below.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditProfileField(
                value = phone,
                label = "Number",
                placeHolder = "Enter your number",
                isError = isError,
                onValueChange = updatePhone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

            Text(
                text = errorMsg ?: " ",
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Transparent
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    .defaultMinSize(minHeight = 24.dp)
                    .wrapContentHeight(align = Alignment.Top)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Confirm",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 16.dp, end = 12.dp)
                        .clickable {
                            onConfirmClick()
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnsurePhonePopupPreview() {
    UPayMiMoniTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            EnsurePhonePopupContent(
                onConfirmClick = {},
                phone = TextFieldValue("88888888"),
                updatePhone = {},
                isError = false,
                errorMsg = "",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnsurePhonePopupPreviewError() {
    UPayMiMoniTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            EnsurePhonePopupContent(
                onConfirmClick = {},
                phone = TextFieldValue(""),
                updatePhone = {},
                isError = true,
                errorMsg = "Please fill in your number."
            )
        }
    }
}

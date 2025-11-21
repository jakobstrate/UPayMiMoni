package com.example.upaymimoni.presentation.ui.groups.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun PersonBanner(
    userId: String,
    name: String,
    phoneNumber: String,
    amountOwed: Double,
    currencyString: String,
    profileImageUrl: String? = null,
    showNotifyButton: Boolean = true,
    onNotifyClick: (String, Double, String) -> Unit = {s1, d1, s2 -> },
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = "Profile picture for user",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                    CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon",
                    modifier = modifier
                        .size(16.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }

            Text(
                text = "${"%.2f".format(amountOwed)} $currencyString",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (showNotifyButton) {
            FloatingActionButton(
                onClick = { onNotifyClick(userId, amountOwed, currencyString) },
                modifier = modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notify Icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonBannerPreview() {
    UPayMiMoniTheme {
        PersonBanner(
            userId = "123",
            name = "Darth Vader",
            phoneNumber = "88 88 88 88",
            amountOwed = 50.0,
            currencyString = "DKK",
            showNotifyButton = true,
        )
    }
}
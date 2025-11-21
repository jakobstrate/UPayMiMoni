package com.example.upaymimoni.presentation.ui.groups.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

data class BannerInformation(
    val profilePictureUrl: String,
    val name: String,
    val phoneNumber: String,
    val amount: Double,
    val userId: String,
)

fun User.toBannerInformation(amount: Double) = BannerInformation(
    profilePictureUrl = this.profilePictureUrl ?: "",
    name = this.displayName ?: "",
    phoneNumber = this.phoneNumber ?: "",
    amount = amount,
    userId = this.id
)

@Composable
fun SettledTitleCard(
    balance: Double,
    totalExpenditure: Double,
    currency: String
) {
    FloatingCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Text(
            text = "Balance",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${"%.2f".format(balance)} $currency",
            style = MaterialTheme.typography.displaySmall,
            color = if (balance >= 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Total Expenditure",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "$totalExpenditure $currency",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SettledGroupPersonsCard(
    label: String,
    banners: List<BannerInformation>,
    currencyString: String,
    showNotifyButton: Boolean = true,
    onNotifyClick: (String, Double, String) -> Unit = {s1, d1, s2 ->},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 24.dp)
        )

        FloatingCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .heightIn(max = 200.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = banners,
                    key = { _, info -> info.userId }
                ) { index, info ->
                    PersonBanner(
                        userId = info.userId,
                        name = info.name,
                        phoneNumber = info.phoneNumber,
                        amountOwed = info.amount,
                        currencyString = currencyString,
                        profileImageUrl = info.profilePictureUrl,
                        showNotifyButton = showNotifyButton,
                        onNotifyClick = onNotifyClick
                    )

                    if (index < banners.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TitleCardPreview() {
    UPayMiMoniTheme {
        SettledTitleCard(
            balance = 123.5,
            totalExpenditure = 340.95,
            currency = "DKK"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettledGroupPersonsCardPreview() {
    val item1 = BannerInformation(
        profilePictureUrl = "http://10.0.2.2:9199/v0/b/upaymimoni.firebasestorage.app/o/profile_images%2F11ZNrpRa1SEpo2pnBizhOGYjQfJM.jpg?alt=media&token=fc04c4b9-82f8-4ad5-83d0-a5e410803d8b",
        name = "Darth Vader",
        phoneNumber = "11223344",
        amount = 50.5,
        userId = "11ZNrpRa1SEpo2pnBizhOGYjQfJM"
    )

    val item2 = BannerInformation(
        profilePictureUrl = "http://10.0.2.2:9199/v0/b/upaymimoni.firebasestorage.app/o/profile_images%2FJVLiw3dilLqC4CVx5LfiJ9dJWTUE.jpg?alt=media&token=f632e115-ca0d-47b8-aeaa-ca1ef223487d",
        name = "Leia Organa",
        phoneNumber = "66450921",
        amount = 120.25,
        userId = "JVLiw3dilLqC4CVx5LfiJ9dJWTUE"
    )
    UPayMiMoniTheme {
        SettledGroupPersonsCard(
            label = "They owe you",
            banners = listOf(item1, item2),
            currencyString = "DKK",
            showNotifyButton = true,
            onNotifyClick = {s1, d1, s2 ->},
        )
    }
}
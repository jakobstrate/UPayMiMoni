package com.example.upaymimoni.presentation.ui.groups.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

@Composable
fun FloatingCard(
    modifier: Modifier = Modifier,
    elevation: CardElevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 8.dp
    ),
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = elevation,
        colors = colors
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FloatingCardPreview() {
    UPayMiMoniTheme {
        FloatingCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Test Title",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
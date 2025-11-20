package com.example.upaymimoni.presentation.ui.expenses.popups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.upaymimoni.presentation.ui.expenses.viewmodel.UserUIData
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

/**
 * Popup for showing and selecting, who paid for expense
 */
@Composable
fun PaidByPopup(options: List<UserUIData>,
                selected: String,
                onSelect: (String) -> Unit,
                onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Surface(shape = MaterialTheme.shapes.medium,
            modifier = Modifier.heightIn(max = 450.dp)) {
            Column {
            // HEADER
                Text(
                    text = "Who PAID????",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 16.dp)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f))
                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                    items(options, key = {it.id}) { item ->
                        // elements of each item
                        val isSelected = item.id == selected

                        Row (modifier =
                            Modifier.padding(horizontal = 18.dp)
                                .clickable {
                                    onSelect(item.id) // Updates state and calls onDismiss via screen logic
                                },
                            verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = item.name,
                                fontWeight = if (isSelected) FontWeight.Bold
                                else FontWeight.Normal,
                                modifier = Modifier.weight(1f).padding(vertical = 18.dp)
                            )
                            if (isSelected) Icon(Icons.Filled.CheckBox,
                                contentDescription = "Check",
                                tint = MaterialTheme.colorScheme.primary)
                        }

                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewPaidByPopup() {
    val users = listOf<UserUIData>(
        UserUIData(
            id = "dGIMqTw20gdGKSqn1tKzpbHn95Us",
            name = "adam"
        ),
        UserUIData(
            id = "dGIMqTw20gdGKSqg3tKzpbHn95Us",
            name = "Crystal"
        ),
        UserUIData(
            id = "dGIfhsdsdfKSqn1tKzpbHn95Us",
            name = "Metha"
        ),
        UserUIData(
            id = "dGI234234tKzpbHn95Us",
            name = "Nick"
        ),
    )

    UPayMiMoniTheme (darkTheme = false) {
        PaidByPopup (
            options = users,
            selected = "crystal",
            onSelect = {},
            onClose = {},
        )
    }
}
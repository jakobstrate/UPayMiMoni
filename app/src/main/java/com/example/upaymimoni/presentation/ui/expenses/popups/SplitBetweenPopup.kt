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
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Button
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
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

/**
 * Popup for showing and selecting, who to split expense among
 */
@Composable
fun SplitBetweenPopup(options: List<String>,
                      selected: Set<String>,
                      onToggleSelection: (String) -> Unit,
                      onClose: () -> Unit,
                      onConfirm: (Set<String>) -> Unit){
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            // control max height
            modifier = Modifier.heightIn(max = 450.dp),
        ) {
            Column {
                // HEADER
                Text(
                    text = "Select users to split between",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 16.dp)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f))
                // List of Checkable Items
                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    items(options) { item ->
                        // elements of each item
                        Row (modifier =
                            Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                                .clickable {
                                onToggleSelection(item) // Updates state and calls onDismiss via screen logic
                            },
                            verticalAlignment = Alignment.CenterVertically){
                            if (selected.contains(item)) Icon(Icons.Filled.CheckBox,
                                contentDescription = "Check",
                                tint = MaterialTheme.colorScheme.primary)
                            else
                                Icon(Icons.Filled.CheckBoxOutlineBlank,
                                contentDescription = "Check",
                                tint = MaterialTheme.colorScheme.primary)

                            Text(
                                text = item,
                                fontWeight = if (selected.contains(item)) FontWeight.Bold
                                else FontWeight.Normal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
                // OK Button
                Button(
                    onClick = {
                        onConfirm(selected)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplitBetweenPopup() {
    UPayMiMoniTheme (darkTheme = false) {
        SplitBetweenPopup(
            options = listOf(
                "Adam",
                "Mack",
                "Nick",
                "Muhammad",
                "nickie",
                "crystal",
                "metha",
                "niels",
                "anders"
            ),
            selected = setOf(
                "Adam",
                "crystal",
                "metha",
            ),
            onToggleSelection = {},
            onClose = {},
            onConfirm = {},
        )
    }
}
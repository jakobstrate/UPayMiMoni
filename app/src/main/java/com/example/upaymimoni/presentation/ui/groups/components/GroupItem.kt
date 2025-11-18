package com.example.upaymimoni.presentation.ui.groups.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.upaymimoni.domain.model.Group



@Composable
fun GroupItem(group: Group) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(group.groupName, fontWeight = FontWeight.SemiBold)
                Text(
                    text = group.id,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Placeholder")
//                Text(
//                    text = if (group.name == "You") "- DKK ${"%.2f".format(group.amount)}"
//                    else "+ DKK ${"%.2f".format(group.amount)}",
//                    fontWeight = FontWeight.Bold,
//                    color = if (group.name == "You") Color(0xFFD32F2F) else Color(0xFF2E7D32)
//                )
//                Text(group.date, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
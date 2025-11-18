package com.example.upaymimoni.presentation.ui.groups.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.R


@Composable
fun InviteButton( modifier: Modifier
) {
    IconButton(
        modifier = Modifier.size(40.dp).then(modifier),
        onClick = { TODO() },
    ) {
        Icon(painter = painterResource(R.drawable.add), contentDescription = "add icon")
    }
}


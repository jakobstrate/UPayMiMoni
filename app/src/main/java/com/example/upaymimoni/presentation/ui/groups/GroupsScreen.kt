package com.example.upaymimoni.presentation.ui.groups

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
//import com.example.upaymimoni.presentation.ui.auth.components.AppLogo
import com.example.upaymimoni.presentation.ui.groups.components.InviteButton


@Preview
@Composable
fun groupsScreen() {
    Column (
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Row (){
            InviteButton()
            Text("Hello")
//            AppLogo()

        }


        Column (){ Text("Next part")  }
        Row (){Text("LastPart")}
    }
}
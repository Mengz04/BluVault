package com.example.bluvault.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable {
                navController.navigate("Dashboard") {
                    launchSingleTop = true
                }

            }
        )
        Text("Card Collection", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Card",
            modifier = Modifier.clickable {
                navController.navigate("CreateCard")
            }
        )

    }
}


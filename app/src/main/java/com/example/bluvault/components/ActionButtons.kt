package com.example.bluvault.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ActionButtons(
    onTransferClick: () -> Unit,
    onTopUpClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDepositClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(Icons.Default.Send, "Transfer", onClick = onTransferClick) // ✅ Hook into click
        ActionButton(Icons.Default.Add, "Topup", onClick = onTopUpClick)
        ActionButton(Icons.Default.Lock, "Deposit", onClick = onDepositClick)
        ActionButton(Icons.Default.Settings, "Setting", onClick = onDeleteClick)
    }
}


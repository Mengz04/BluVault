package com.example.bluvault.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.bluvault.operations.CardData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownCardSelector(
    cards: List<CardData>,
    userMap: Map<String, String>, // 👈 Add this parameter
    onCardSelected: (CardData) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Select") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Card") },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(16.dp), // ✅ Rounded corners without clipping
                modifier = Modifier.fillMaxWidth()
            )

            // Transparent overlay to make entire field clickable
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .clickable { expanded = !expanded }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp)) // ✅ Dropdown rounded
                .padding(vertical = 4.dp) // spacing between items
        ) {
            cards.forEach { card ->
                val ownerName = userMap[card.ownerUid] ?: card.ownerUid.take(6)

                DropdownMenuItem(
                    onClick = {
                        selectedText = "${card.cardName} •••• ${card.cardNumber.takeLast(4)}"
                        expanded = false
                        onCardSelected(card)
                    },
                    text = {
                        Column {
                            Text(
                                "${card.cardName} •••• ${card.cardNumber.takeLast(4)}",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Owner: $ownerName",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 6.dp) // ✅ item spacing
                )
            }
        }
    }

}

package com.example.bluvault.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.operations.TransactionData
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun TransactionList(transactions: List<TransactionData>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        if (transactions.isEmpty()) {
            // ⛔ No transactions text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No transactions yet",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            Column {
                // 🔺 Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Transactions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "${transactions.size} transaction${if (transactions.size == 1) "" else "s"} today",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }


                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }

                    // ⬇️ Extra spacer at the bottom
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Adjust as needed
                    }
                }
            }
        }
    }
}

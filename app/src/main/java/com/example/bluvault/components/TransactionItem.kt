package com.example.bluvault.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.operations.TransactionData
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionItem(transaction: TransactionData) {
    val date = transaction.timestamp?.toDate()
    val formattedDate = date?.let {
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(it)
    } ?: "--/--/--"

    val formattedTime = date?.let {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(it)
    } ?: "--:--"

    val isIncome = transaction.type == "Money in"
    val amountColor = if (isIncome) Color(0xFF2ECC71) else Color.Red

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔘 Left: Icon + Title + Type
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(2f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC107)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Transaction Icon",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = transaction.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (isIncome) "Money In" else "Money Out",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 📅 Middle: Date and Time
            Column(
                modifier = Modifier.weight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = formattedTime,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 💰 Right: Amount
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "$ ${NumberFormat.getNumberInstance(Locale("en", "US")).format(transaction.amount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = amountColor
                )
            }
        }
    }
}

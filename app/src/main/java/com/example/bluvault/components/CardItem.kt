package com.example.bluvault.components

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.operations.CardData
import com.example.bluvault.R
import kotlin.math.absoluteValue

@Composable
fun CardItem(
    card: CardData,
    isSelected: Boolean = false,
    username: String = "Card Holder"
) {
    val elevation = if (isSelected) 12.dp else 4.dp
    val scale = if (isSelected) 1f else 0.95f

    val gradientOptions = listOf(
        listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF)),
        listOf(Color(0xFF0F2027), Color(0xFF2C5364)),
        listOf(Color(0xFF232526), Color(0xFF414345)),
        listOf(Color(0xFFbdc3c7), Color(0xFF2c3e50)),
        listOf(Color(0xFFB993D6), Color(0xFF8CA6DB)),
        listOf(Color(0xFFff9966), Color(0xFFff5e62)),
        listOf(Color(0xFFc2e59c), Color(0xFF64b3f4)),
        listOf(Color(0xFFee9ca7), Color(0xFFffdde1)),
        listOf(Color(0xFF606c88), Color(0xFF3f4c6b))
    )

    val backgroundColors = remember(card.id) {
        gradientOptions[card.id.hashCode().absoluteValue % gradientOptions.size]
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(backgroundColors))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Username
                    Column {
                        Text(
                            text = card.cardName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Visa Logo
                    Image(
                        painter = painterResource(id = R.drawable.visa_logo), // Replace with your Visa logo
                        contentDescription = "Visa Logo",
                        modifier = Modifier.height(24.dp)
                    )
                }

                // NFC & Card number (dummy)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📶", color = Color.White, fontSize = 16.sp) // Dummy NFC
                    Text("•••• ${card.cardNumber.takeLast(4)}", color = Color.White)
                }

                // Expiry + Balance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Valid thru", color = Color.LightGray, fontSize = 12.sp)
                        Text("04/27", color = Color.White, fontSize = 14.sp)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Balance", color = Color.LightGray, fontSize = 12.sp)
                        Text(
                            text = "$${"%,.2f".format(card.balance)}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

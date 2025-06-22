package com.example.bluvault.pages

import FamilijenGrotesk
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bluvault.R

@Composable
fun TopUpScreen(navController: NavHostController) {
    val netflixPlan = "Netflix Premium 4K"
    val priceInRupiah = "Rp 230.000,00"
    val monthlyPrice = "US\$22.99"
    val platformParticipation = "0.2%"
    val source = "Debit Card"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Common AppBar height, adjust as needed
            contentAlignment = Alignment.Center // Centers all content within the Box
        ) {
            // Back button on the left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart), // Aligns this Row to the start and vertically center within the Box
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("Welcome") },
                    modifier = Modifier
                        .height(44.dp)
                        .width(44.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = "left arrow",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            // Text centered horizontally
            Text(
                text = "Single Payment",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.Center) // This centers the Text within the Box
                    .padding(horizontal = 48.dp), // Add padding to prevent text overlap with button if it gets too long
                fontFamily = FamilijenGrotesk
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card for Payment Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Netflix Plan Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = netflixPlan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = priceInRupiah,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Source Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Source")
                    Text(text = source)
                    TextButton(onClick = { /* Handle change */ }) {
                        Text(text = "Change", color = Color(0xFF6200EE))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Monthly price")
                        Text(text = monthlyPrice)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Platform participation")
                        Text(text = platformParticipation)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Authorize Button
        Button(
            onClick = { /* Handle authorize */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Authorize", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cashback Info
        Text(
            text = "Get cashback 2% with Paywave+",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
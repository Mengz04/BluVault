package com.example.bluvault.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bluvault.R

@Composable
fun BottomNavBar(
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    val icons = listOf(
        Pair(painterResource(id = R.drawable.home), "Home"),
        Pair(painterResource(id = R.drawable.card), "Card"),
        Pair(painterResource(id = R.drawable.qr_icon), "QRIS"),
        Pair(painterResource(id = R.drawable.frame), "transfer"),
        Pair(painterResource(id = R.drawable.profile_circle), "Profile")
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 16.dp, start = 32.dp, end = 32.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF030B1C),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEachIndexed { index, (icon, label) ->
                    val isSelected = index == selectedIndex

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) Color(0xFF4A4A4A) else Color.Transparent)
                            .clickable { onItemSelected(index) }
                            .padding(horizontal = if (isSelected) 16.dp else 0.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = icon,
                                contentDescription = label,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                            if (isSelected) {
                                Text(
                                    text = label,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

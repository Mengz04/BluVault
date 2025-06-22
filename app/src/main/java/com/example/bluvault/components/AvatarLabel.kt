package com.example.bluvault.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.R

@Composable
fun AvatarLabel(
    label: String,
) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "default user",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

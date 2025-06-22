package com.example.bluvault.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val avatarOptions = listOf(
        R.drawable.u1,
        R.drawable.u2,
        R.drawable.u3,
        R.drawable.u4,
        R.drawable.default_user
    )

    // Pick one randomly
    val avatarId = remember (label) {
        avatarOptions.random()
    }

    Column(
        modifier = Modifier
            .height(64.dp)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = avatarId),
            contentDescription = "user avatar",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(CircleShape)
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


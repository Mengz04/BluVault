package com.example.bluvault.components

import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.roundToInt

@Composable
fun SwipeToConfirm(
    text: String,
    enabled: Boolean,
    onSwipe: () -> Unit
) {
    val swipeWidth = 56.dp
    val trackWidth = remember { mutableStateOf(0f) }
    val offsetX = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val backgroundColor by animateColorAsState(
        if (enabled) Color(0xFF0F1F21) else Color.Gray,
        label = "Background"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .onGloballyPositioned() {
                trackWidth.value = it.size.width.toFloat()
            }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures (
                    onDragEnd = {
                        scope.launch {
                            val maxSwipe = trackWidth.value - with(density) { swipeWidth.toPx() }
                            if (offsetX.value > maxSwipe * 0.8f) {
                                onSwipe()
                            }
                            offsetX.animateTo(0f)
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    val maxSwipe = trackWidth.value - with(density) { swipeWidth.toPx() }
                    scope.launch {
                        val newValue = (offsetX.value + dragAmount.x).coerceIn(0f, maxSwipe)
                        offsetX.snapTo(newValue)
                    }
                }
            }
    ) {
        // Draggable Icon
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .padding(4.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.Black
            )
        }

        // Text Centered
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "slide to confirm",
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }
    }
}

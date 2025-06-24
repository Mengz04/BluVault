package com.example.bluvault.pages

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun QRISScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var preview by remember { mutableStateOf<Preview?>(null) }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(250.dp) // QR frame size
        ) {
            val stroke = 6.dp
            val cornerLength = 32.dp
            val frameColor = Color.Green

            Canvas (modifier = Modifier.fillMaxSize()) {
                val strokePx = stroke.toPx()
                val lengthPx = cornerLength.toPx()

                // Top-left
                drawLine(
                    color = frameColor,
                    start = Offset(0f, 0f),
                    end = Offset(lengthPx, 0f),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = frameColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, lengthPx),
                    strokeWidth = strokePx
                )

                // Top-right
                drawLine(
                    color = frameColor,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width - lengthPx, 0f),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = frameColor,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, lengthPx),
                    strokeWidth = strokePx
                )

                // Bottom-left
                drawLine(
                    color = frameColor,
                    start = Offset(0f, size.height),
                    end = Offset(lengthPx, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = frameColor,
                    start = Offset(0f, size.height),
                    end = Offset(0f, size.height - lengthPx),
                    strokeWidth = strokePx
                )

                // Bottom-right
                drawLine(
                    color = frameColor,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width - lengthPx, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = frameColor,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width, size.height - lengthPx),
                    strokeWidth = strokePx
                )
            }
        }
    }
}
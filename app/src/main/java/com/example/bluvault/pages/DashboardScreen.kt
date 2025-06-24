package com.example.bluvault.pages

import FamilijenGrotesk
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.example.bluvault.components.AvatarLabel
import com.example.bluvault.components.EWalletItem
import kotlin.String
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import com.example.bluvault.components.BottomNavBar
import com.example.bluvault.operations.UserSummary
import com.example.bluvault.operations.fetchTotalCardBalance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.bluvault.operations.fetchLatestUsernames
import kotlin.math.roundToInt

data class EWalletData(
    val icon: Painter,
    val label: String,
    val backgroundColor: Color,
    val balance: Double? = null
)


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    var selectedIndex by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    var recentUsers by remember { mutableStateOf<List<UserSummary>>(emptyList()) }
    var totalBalance by remember { mutableStateOf<Double?>(null) }
    var isLoadingUsers by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }
    var walletBalances by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }


    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    LaunchedEffect(uid) {
        uid?.let {
            fetchTotalCardBalance(
                uid = it,
                db = db,
                onSuccess = { totalBalance = it },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    LaunchedEffect(uid) {
        uid?.let { safeUid ->
            fetchLatestUsernames(
                db = db,
                currentUid = safeUid,
                onSuccess = {
                    recentUsers = it
                    isLoadingUsers = false
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    isLoadingUsers = false
                }
            )
        }
    }


    val wallets = listOf(
        EWalletData(painterResource(id = R.drawable.gopey), "HoPay", Color(0xFF27AE60), walletBalances["HoPay"]),
        EWalletData(painterResource(id = R.drawable.urlaja), "URLaja", Color(0xffff2c2c), walletBalances["URLaja"]),
        EWalletData(painterResource(id = R.drawable.sopipey), "SopiPey", Color(0xFFEB7C17), walletBalances["SopiPey"]),
        EWalletData(painterResource(id = R.drawable.dono), "Dono", Color(0xFF3EB7DB), walletBalances["Dono"])
    )

    LaunchedEffect(uid) {
        uid?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { doc ->
                    val balances = mapOf(
                        "HoPay" to (doc.getDouble("hopay_balance") ?: 0.0),
                        "URLaja" to (doc.getDouble("urlaja_balance") ?: 0.0),
                        "SopiPey" to (doc.getDouble("sopipey_balance") ?: 0.0),
                        "Dono" to (doc.getDouble("dono_balance") ?: 0.0)
                    )
                    walletBalances = balances
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch wallet balances", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.bluvault_dark_icon),
                contentDescription = "bluVault logo",
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            )

            Box {
                Image(
                    painter = painterResource(id = R.drawable.default_user),
                    contentDescription = "default user",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(CircleShape)
                        .clickable() { showMenu = !showMenu }
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 4.dp)
                        .width(150.dp) // Optional: control width
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Logout",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            showMenu = false
                            navController.navigate("Login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout icon",
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

            }

        }

        // Balance
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(
                text = "Total Balance",
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (totalBalance == null) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(28.dp), // adjust size as needed
                    strokeWidth = 2.dp // optional
                )
            } else {
                Text(
                    text = "$${"%.2f".format(totalBalance)}",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 38.sp,
                    fontFamily = FamilijenGrotesk
                )
            }
        }

        // E-money
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "E-money Balance",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp), // slightly more height for spacing
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(wallets) { wallet ->
                        EWalletItem(
                            icon = wallet.icon,
                            label = wallet.label,
                            backgroundColor = wallet.backgroundColor,
                            balance = wallet.balance // ✅ pass the balance here
                        )
                    }
                }
            }
        }


        // Quick Transfer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick transfer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                when {
                    isLoadingUsers -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.Black,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    recentUsers.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No users available yet",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    else -> {
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 76.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(recentUsers) { user ->
                                AvatarLabel(
                                    label = user.username,
                                    onClick = {
                                        navController.navigate("transfer?receiverUid=${user.uid}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }


        // Exchange Rate
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Exchange Rate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "1 EUR = 1.08315 USD",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExchangeRateChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("A month ago", fontSize = 12.sp, color = Color.Gray)
                    Text("Today", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ExchangeRateChart(
    modifier: Modifier = Modifier,
    points: List<Float> = listOf(0.6f, 0.65f, 0.55f, 0.58f, 0.6f, 0.7f, 0.68f, 0.66f, 0.72f)
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier
            .pointerInput(points) {
                detectTapGestures() { offset ->
                    val stepX = size.width / (points.size - 1)
                    val tappedIndex = (offset.x / stepX).roundToInt().coerceIn(0, points.lastIndex)
                    selectedIndex = tappedIndex
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val stepX = width / (points.size - 1)

            val path = Path()
            points.forEachIndexed { index, value ->
                val x = stepX * index
                val y = height - (value * height)
                if (index == 0) path.moveTo(x, y)
                else path.lineTo(x, y)
            }

            // Line stroke
            drawPath(
                path = path,
                color = Color(0xFF3CA4B9),
                style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Fill under curve
            path.lineTo(width, height)
            path.lineTo(0f, height)
            path.close()

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3CA4B9).copy(alpha = 0.3f), Color.Transparent),
                    startY = 0f,
                    endY = height
                )
            )

            // Y-axis labels on the right
            val maxValue = points.maxOrNull() ?: 1f
            val minValue = points.minOrNull() ?: 0f
            val stepY = (maxValue - minValue) / 4

            for (i in 0..4) {
                val yValue = minValue + i * stepY
                val y = height - ((yValue - minValue) / (maxValue - minValue)) * height

                drawContext.canvas.nativeCanvas.drawText(
                    "%.3f".format(yValue),
                    size.width - 50f,
                    y,
                    android.graphics.Paint().apply {
                        textSize = 28f
                        color = android.graphics.Color.GRAY
                    }
                )
            }

            // Draw selected point and tooltip
            selectedIndex?.let { index ->
                if (index in points.indices) {
                    val value = points[index]
                    val x = stepX * index
                    val y = height - (value * height)

                    drawCircle(
                        color = Color.Black,
                        radius = 8f,
                        center = Offset(x, y)
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        "1 EUR = ${"%.3f".format(value)} USD",
                        x,
                        y - 20f,
                        android.graphics.Paint().apply {
                            textSize = 32f
                            color = android.graphics.Color.BLACK
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }
    }
}


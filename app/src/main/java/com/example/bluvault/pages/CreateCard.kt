package com.example.bluvault.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.components.SwipeToConfirm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CreateCardPage(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var cardName by remember { mutableStateOf("") }
    var successState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )
            Text("Create Card", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Card Name")
                    OutlinedTextField(
                        value = cardName,
                        onValueChange = { cardName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val isReady = cardName.isNotBlank() && uid != null

                    if (successState) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF0F1F21)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.White
                            )
                        }
                    } else {
                        SwipeToConfirm (
                            text = "Create Card",
                            enabled = isReady,
                            onSwipe = {
                                if (uid == null || cardName.isBlank()) {
                                    Toast.makeText(context, "Please fill in the card name", Toast.LENGTH_SHORT).show()
                                    return@SwipeToConfirm
                                }

                                val newCard = hashMapOf(
                                    "cardName" to cardName,
                                    "balance" to 0.0,
                                    "currency" to "USD",
                                    "ownerUid" to uid
                                )

                                db.collection("users")
                                    .document(uid)
                                    .collection("cards")
                                    .add(newCard)
                                    .addOnSuccessListener {
                                        Toast.makeText(context.applicationContext, "Card created!", Toast.LENGTH_SHORT).show()
                                        successState = true
                                        scope.launch {
                                            delay(1000)
                                            navController.popBackStack()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message ?: "Failed to create card", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}

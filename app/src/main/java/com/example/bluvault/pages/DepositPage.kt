package com.example.bluvault.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.bluvault.components.DropdownCardSelector
import com.example.bluvault.components.SwipeToConfirm
import com.example.bluvault.operations.CardData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DepositPage(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var allCards by remember { mutableStateOf<List<CardData>>(emptyList()) }
    var selectedCard by remember { mutableStateOf<CardData?>(null) }
    var userMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var amount by remember { mutableStateOf("") }
    var successState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Load cards and user names
    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect

        db.collection("users").get().addOnSuccessListener { users ->
            userMap = users.associate { it.id to (it.getString("username") ?: "Unknown") }

            db.collectionGroup("cards").get().addOnSuccessListener { cardDocs ->
                val cards = cardDocs.mapNotNull { doc ->
                    val ownerUid = doc.reference.path.split("/")[1]
                    doc.toObject(CardData::class.java).copy(cardNumber = doc.id, ownerUid = ownerUid)
                }.filter { it.ownerUid == uid }

                allCards = cards
                if (selectedCard == null && cards.isNotEmpty()) {
                    selectedCard = cards.first()
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
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
            Text("Deposit", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card (
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Select Card")
                    if (allCards.isEmpty()) {
                        Text("No cards available", color = Color.Gray)
                    } else {
                        DropdownCardSelector(
                            cards = allCards,
                            onCardSelected = { selectedCard = it },
                            userMap = userMap
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Amount")
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        leadingIcon = { Text("US$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val allInputsFilled = selectedCard != null && amount.toDoubleOrNull() != null && amount.toDouble() > 0

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
                        SwipeToConfirm(
                            text = "Deposit US$$amount",
                            enabled = allInputsFilled,
                            onSwipe = {
                                val amt = amount.toDoubleOrNull()
                                val card = selectedCard
                                if (amt == null || amt <= 0.0 || card == null || uid == null) {
                                    Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show()
                                    return@SwipeToConfirm
                                }

                                val cardRef = db.collection("users")
                                    .document(uid)
                                    .collection("cards")
                                    .document(card.cardNumber)

                                db.runTransaction { transaction ->
                                    val snapshot = transaction.get(cardRef)
                                    val currentBalance = snapshot.getDouble("balance") ?: 0.0

                                    // ➕ Create transaction
                                    val trans = hashMapOf(
                                        "title" to "Deposit",
                                        "type" to "Money in",
                                        "amount" to amt,
                                        "currency" to "USD",
                                        "timestamp" to FieldValue.serverTimestamp(),
                                        "notes" to null
                                    )
                                    val transRef = cardRef.collection("transactions").document()
                                    transaction.set(transRef, trans)

                                    // ➕ Update balance
                                    transaction.update(cardRef, "balance", currentBalance + amt)
                                }.addOnSuccessListener {
                                    Toast.makeText(context.applicationContext, "Deposit successful", Toast.LENGTH_SHORT).show()
                                    successState = true
                                    scope.launch {
                                        delay(1000)
                                        navController.popBackStack()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(context, it.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                    }
                }
            }
        }
    }
}

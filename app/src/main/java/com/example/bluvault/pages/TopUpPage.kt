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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.bluvault.operations.CardData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluvault.components.DropdownCardSelector
import com.example.bluvault.components.DropdownSelector
import com.example.bluvault.components.SwipeToConfirm
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TopUpPage(
    navController: NavHostController,
    activeCard: CardData?
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var allCards by remember { mutableStateOf<List<CardData>>(emptyList()) }
    var selectedCard by remember { mutableStateOf<CardData?>(activeCard) }
    var selectedEwallet by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var userMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var successState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val ewalletOptions = listOf("SopiPey", "Dono", "HoPay", "URLAja")

    // 🔁 Load cards and user map
    LaunchedEffect(uid) {
        db.collection("users").get().addOnSuccessListener { users ->
            userMap = users.associate { it.id to (it.getString("username") ?: "Unknown") }

            db.collectionGroup("cards").get().addOnSuccessListener { result ->
                val cards = result.mapNotNull {
                    val ownerUid = it.reference.path.split("/")[1]
                    it.toObject(CardData::class.java).copy(cardNumber = it.id, ownerUid = ownerUid)
                }
                allCards = cards.filter { it.ownerUid == uid }
                if (selectedCard == null && allCards.isNotEmpty()) {
                    selectedCard = allCards.first()
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
                    .clickable{ navController.popBackStack() }
            )
            Text("Top Up", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card (
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("E-Wallet", fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(12.dp))
                    DropdownSelector(
                        label = "Choose E-Wallet",
                        options = ewalletOptions,
                        selectedOption = selectedEwallet,
                        onOptionSelected = { selectedEwallet = it }
                    )

                    Spacer(Modifier.height(12.dp))
                    Text("From (Your Card)")
                    if (allCards.isEmpty()) {
                        Text("No available cards", color = Color.Gray)
                    } else {
                        DropdownCardSelector (
                            cards = allCards,
                            onCardSelected = { selectedCard = it },
                            userMap = userMap
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Amount")
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        leadingIcon = { Text("US$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(24.dp))
                    val allInputsFilled = amount.toDoubleOrNull() != null &&
                            amount.toDouble() > 0 &&
                            selectedCard != null &&
                            selectedEwallet.isNotBlank()

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
                            text = "Top Up ${selectedEwallet} US$$amount",
                            enabled = allInputsFilled,
                            onSwipe = {
                                val amt = amount.toDoubleOrNull()
                                val card = selectedCard
                                if (amt == null || amt <= 0.0 || card == null || uid == null) {
                                    Toast.makeText(context, "Please complete all fields", Toast.LENGTH_SHORT).show()
                                    return@SwipeToConfirm
                                }

                                val userRef = db.collection("users").document(uid)
                                val cardRef = db.collection("users").document(uid)
                                    .collection("cards").document(card.cardNumber)

                                db.runTransaction { transaction ->
                                    // 🔁 All reads first
                                    val cardSnapshot = transaction.get(cardRef)
                                    val userSnap = transaction.get(userRef)

                                    val currentBalance = cardSnapshot.getDouble("balance") ?: 0.0
                                    if (currentBalance < amt) {
                                        throw Exception("Insufficient balance")
                                    }

                                    val ewalletField = when (selectedEwallet) {
                                        "SopiPey" -> "sopipey_balance"
                                        "Dono" -> "dono_balance"
                                        "HoPay" -> "hopay_balance"
                                        "URLAja" -> "urlaja_balance"
                                        else -> throw IllegalArgumentException("Invalid e-wallet")
                                    }

                                    val currentEwallet = userSnap.getDouble(ewalletField) ?: 0.0

                                    // ✅ All writes after reads
                                    val trans = hashMapOf(
                                        "title" to "Top Up $selectedEwallet",
                                        "type" to "Money out",
                                        "amount" to -amt,
                                        "timestamp" to FieldValue.serverTimestamp(),
                                        "notes" to null
                                    )
                                    val transRef = cardRef.collection("transactions").document()
                                    transaction.set(transRef, trans)

                                    transaction.update(cardRef, "balance", currentBalance - amt)
                                    transaction.update(userRef, ewalletField, currentEwallet + amt)
                                }.addOnSuccessListener {
                                    Toast.makeText(context.applicationContext, "Top up successful", Toast.LENGTH_SHORT).show()
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

package com.example.bluvault.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.bluvault.components.SwipeToConfirm
import com.example.bluvault.operations.performTransfer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TransferPage(
    navController: NavHostController,
    activeCard: CardData?, // Nullable for dashboard case
    receiverUid: String? = null
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var allCards by remember { mutableStateOf<List<CardData>>(emptyList()) }
    var selectedCard by remember { mutableStateOf<CardData?>(null) } // Receiver card
    var selectedSenderCard by remember { mutableStateOf<CardData?>(activeCard) } // Sender card
    var amount by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var userMap by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var successState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 🔁 Load all cards & user map
    LaunchedEffect(uid, receiverUid) {
        db.collection("users")
            .get()
            .addOnSuccessListener { userDocs ->
                val map = userDocs.associate { it.id to (it.getString("username") ?: "Unknown") }
                userMap = map

                db.collectionGroup("cards")
                    .get()
                    .addOnSuccessListener { result ->
                        val cards = result.mapNotNull { doc ->
                            val ownerUid = doc.reference.path.split("/")[1]
                            val cardData = doc.toObject(CardData::class.java)
                            cardData.copy(cardNumber = doc.id, ownerUid = ownerUid)
                        }

                        // Separate sender's cards and filtered receiver cards
                        allCards = cards

                        if (selectedSenderCard == null) {
                            val senderCards = cards.filter { it.ownerUid == uid }
                            if (senderCards.isNotEmpty()) {
                                selectedSenderCard = senderCards.first() // optional pre-selection
                            }
                        }
                    }
            }
    }

    Column(
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
            Text("Transfer", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Card
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Account", fontWeight = FontWeight.Bold)
                    Text("Transfer to other card", fontSize = 13.sp, color = Color.Gray)

                    Spacer(Modifier.height(16.dp))

                    // 🔁 FROM (sender card)
                    Text("From")
                    if (selectedSenderCard == null || allCards.none { it.ownerUid == uid }) {
                        Text("No available sender cards", color = Color.Gray)
                    } else {
                        DropdownCardSelector(
                            cards = allCards.filter { it.ownerUid == uid },
                            onCardSelected = { selectedSenderCard = it },
                            userMap = userMap
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // 🔁 TO (receiver card)
                    Text("To")
                    DropdownCardSelector(
                        cards = allCards.filter {
                            val isNotSender = it.cardNumber != selectedSenderCard?.cardNumber
                            val isValidReceiver = receiverUid == null || it.ownerUid == receiverUid
                            isNotSender && isValidReceiver
                        },
                        onCardSelected = { selectedCard = it },
                        userMap = userMap
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔁 AMOUNT
                    Text("Amount")
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        leadingIcon = { Text("US$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔁 DETAILS
                    Text("Transaction Details")
                    OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    // 🔁 Confirm
                    val allInputsFilled = amount.toDoubleOrNull() != null &&
                            amount.toDouble() > 0 &&
                            selectedCard != null &&
                            selectedSenderCard != null &&
                            details.isNotBlank()

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
                            text = "Send US$$amount",
                            enabled = allInputsFilled,
                            onSwipe = {
                                val amt = amount.toDoubleOrNull()
                                if (amt == null || amt <= 0.0 || selectedCard == null || selectedSenderCard == null) {
                                    Toast.makeText(context, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
                                    return@SwipeToConfirm
                                }

                                val receiverUidFinal = selectedCard!!.ownerUid
                                val senderUid = uid ?: return@SwipeToConfirm
                                val senderName = userMap[senderUid] ?: "Unknown"
                                val receiverName = userMap[receiverUidFinal] ?: "Unknown"

                                performTransfer(
                                    db = db,
                                    senderUid = senderUid,
                                    receiverUid = receiverUidFinal,
                                    senderCard = selectedSenderCard!!,
                                    receiverCard = selectedCard!!,
                                    amount = amt,
                                    details = details,
                                    context = context,
                                    senderName = senderName,
                                    receiverName = receiverName
                                )

                                successState = true
                                scope.launch {
                                    delay(1000)
                                    navController.popBackStack() // ✅ always works
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

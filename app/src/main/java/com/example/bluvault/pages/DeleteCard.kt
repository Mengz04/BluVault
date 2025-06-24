package com.example.bluvault.pages

import android.widget.Space
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.components.SwipeToConfirm
import com.example.bluvault.operations.CardData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeleteCardPage(
    navController: NavHostController,
    activeCard: CardData
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var successState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
                    .clickable { navController.popBackStack() }
            )
            Text("Delete Card", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card (
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Card Name")
                    OutlinedTextField(
                        value = activeCard.cardName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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
                                contentDescription = "Deleted",
                                tint = Color.White
                            )
                        }
                    } else {
                        SwipeToConfirm (
                            text = "Delete Card",
                            enabled = uid != null,
                            onSwipe = {
                                if (uid == null) {
                                    Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                                    return@SwipeToConfirm
                                }

                                val cardRef = db.collection("users")
                                    .document(uid)
                                    .collection("cards")
                                    .document(activeCard.id)

                                cardRef.delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context.applicationContext, "Card deleted!", Toast.LENGTH_SHORT).show()
                                        successState = true
                                        scope.launch {
                                            delay(1000)
                                            navController.popBackStack()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message ?: "Failed to delete card", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}

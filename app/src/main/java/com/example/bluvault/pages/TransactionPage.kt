package com.example.bluvault.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bluvault.components.TransactionList
import com.example.bluvault.operations.TransactionData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction

@Composable
fun TransactionPage(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var transactions by remember { mutableStateOf<List<TransactionData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect

        db.collection("users")
            .document(uid)
            .collection("cards")
            .get()
            .addOnSuccessListener { cardDocs ->
                val cardIds = cardDocs.map { it.id }

                if (cardIds.isEmpty()) {
                    isLoading = false
                    return@addOnSuccessListener
                }

                val allTransactionTasks = cardIds.map { cardId ->
                    db.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("transactions")
                        .get()
                }

                Tasks.whenAllSuccess<QuerySnapshot>(allTransactionTasks)
                    .addOnSuccessListener { snapshotList ->
                        val allTransactions = snapshotList
                            .flatMap { snapshot ->
                                snapshot.documents.mapNotNull { doc ->
                                    doc.toObject(TransactionData::class.java)
                                }
                            }
                            .sortedByDescending { it.timestamp } // optional sorting

                        transactions = allTransactions
                        isLoading = false
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to load transactions", Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            TransactionList(transactions)
        }
    }
}
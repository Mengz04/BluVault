package com.example.bluvault.operations

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

fun addCardToUser(
    uid: String,
    db: FirebaseFirestore,
    cardName: String,
    balance: Double,
    currency: String = "USD",
    onSuccess: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) {
    val cardData = hashMapOf(
        "cardName" to cardName,
        "balance" to balance,
        "currency" to currency
    )

    db.collection("users")
        .document(uid)
        .collection("cards")
        .add(cardData)
        .addOnSuccessListener {
            onSuccess?.invoke()
        }
        .addOnFailureListener { e ->
            onError?.invoke(e.localizedMessage ?: "Error adding card")
        }
}

fun fetchTotalCardBalance(
    uid: String,
    db: FirebaseFirestore,
    onSuccess: (Double) -> Unit,
    onError: (String) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("cards")
        .get()
        .addOnSuccessListener { result ->
            var total = 0.0
            for (doc in result) {
                total += doc.getDouble("balance") ?: 0.0
            }
            onSuccess(total)
        }
        .addOnFailureListener { e ->
            onError(e.localizedMessage ?: "Failed to fetch cards")
        }
}


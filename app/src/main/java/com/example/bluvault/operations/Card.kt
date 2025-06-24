package com.example.bluvault.operations

import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardData(
    val id: String = "",
    val balance: Double = 0.0,
    val cardNumber: String = "",
    val cardName: String = "",
    val expiry: String = "",
    val ownerUid: String = ""
) : Parcelable

data class TransactionData(
    val title: String = "",
    val amount: Double = 0.0,
    val timestamp: com.google.firebase.Timestamp? = null,
    val type: String = "",
    val notes: String = ""
)

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
        "currency" to currency,
        "ownerUid" to uid // ✅ add this line
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

fun fetchUserCards(uid: String, db: FirebaseFirestore, onSuccess: (List<CardData>) -> Unit) {
    db.collection("users").document(uid).collection("cards")
        .get()
        .addOnSuccessListener { result ->
            val cards = result.map { it.toObject(CardData::class.java).copy(id = it.id) }
            onSuccess(cards)
        }
}

fun fetchTransactions(uid: String, cardId: String, db: FirebaseFirestore, onSuccess: (List<TransactionData>) -> Unit) {
    db.collection("users").document(uid).collection("cards").document(cardId).collection("transactions")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener { result ->
            val txns = result.map { it.toObject(TransactionData::class.java) }
            onSuccess(txns)
        }
}

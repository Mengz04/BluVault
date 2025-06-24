package com.example.bluvault.operations

import android.widget.Toast
import android.content.Context // ✅ CORRECT for Android apps
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

fun performTransfer(
    db: FirebaseFirestore,
    senderUid: String,
    receiverUid: String,
    senderCard: CardData,
    receiverCard: CardData,
    amount: Double,
    details: String,
    context: Context,
    senderName: String,
    receiverName: String
) {
    if (senderCard.balance < amount) {
        Toast.makeText(context, "Insufficient balance", Toast.LENGTH_SHORT).show()
        return
    }

    // ❗ Check for valid document IDs
    if (senderCard.cardNumber.isBlank() || receiverCard.cardNumber.isBlank()) {
        Toast.makeText(context, "Invalid card ID", Toast.LENGTH_SHORT).show()
        return
    }

    val senderRef = db.collection("users").document(senderUid)
        .collection("cards").document(senderCard.cardNumber)

    val receiverRef = db.collection("users").document(receiverUid)
        .collection("cards").document(receiverCard.cardNumber)

    db.runBatch { batch ->
        val timestamp = Timestamp.now()

        batch.set(senderRef.collection("transactions").document(), mapOf(
            "amount" to -amount,
            "notes" to details,
            "timestamp" to timestamp,
            "type" to "Money out",
            "title" to "Transfer to $receiverName"
        ))

        batch.set(receiverRef.collection("transactions").document(), mapOf(
            "amount" to amount,
            "notes" to details,
            "timestamp" to timestamp,
            "type" to "Money in",
            "title" to "Transfer from $senderName"
        ))

        batch.update(senderRef, "balance", senderCard.balance - amount)
        batch.update(receiverRef, "balance", receiverCard.balance + amount)
    }.addOnSuccessListener {
        Toast.makeText(context.applicationContext, "Transfer successful", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {
        Toast.makeText(context.applicationContext, "Transfer failed", Toast.LENGTH_SHORT).show()
    }
}


package com.example.bluvault.operations

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

fun fetchLatestUsernames(
    db: FirebaseFirestore,
    currentUid: String,
    limit: Long = 4,
    onSuccess: (List<String>) -> Unit,
    onError: (String) -> Unit
) {
    db.collection("users")
        .orderBy("createdAt", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener { result ->
            // Filter out current user and get usernames
            val usernames = result
                .filter { it.getString("uid") != currentUid }
                .mapNotNull { it.getString("username") }
                .take(limit.toInt())

            onSuccess(usernames)
        }
        .addOnFailureListener { e ->
            onError(e.localizedMessage ?: "Failed to fetch latest users")
        }
}


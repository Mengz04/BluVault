package com.example.bluvault.operations

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class UserSummary(
    val uid: String,
    val username: String
)

fun fetchLatestUsernames(
    db: FirebaseFirestore,
    currentUid: String,
    limit: Long = 4,
    onSuccess: (List<UserSummary>) -> Unit,
    onError: (String) -> Unit
) {
    db.collection("users")
        .orderBy("createdAt", Query.Direction.DESCENDING)
        .get()
        .addOnSuccessListener { result ->
            val users = result
                .filter { it.getString("uid") != currentUid }
                .mapNotNull { doc ->
                    val uid = doc.getString("uid")
                    val username = doc.getString("username")
                    if (uid != null && username != null) {
                        UserSummary(uid = uid, username = username)
                    } else null
                }
                .take(limit.toInt())

            onSuccess(users)
        }
        .addOnFailureListener { e ->
            onError(e.localizedMessage ?: "Failed to fetch latest users")
        }
}



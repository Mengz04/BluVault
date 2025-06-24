import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.bluvault.operations.addCardToUser
import com.google.firebase.firestore.FieldValue


suspend fun Register(
    context: Context,
    navController: NavHostController,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    dateOfBirth: String,
    selectedGender: String,
    username: String,
    phoneNumber: String,
    onLoadingChange: (Boolean) -> Unit
)
{
    try {
        onLoadingChange(true)

        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user

        if (user != null) {
            val userDetails = hashMapOf(
                "uid" to user.uid,
                "firstName" to firstName,
                "lastName" to lastName,
                "dateOfBirth" to dateOfBirth,
                "gender" to selectedGender,
                "username" to username,
                "email" to email,
                "phoneNumber" to phoneNumber,
                "createdAt" to FieldValue.serverTimestamp(),
                "hopay_balance" to 0.0,
                "urlaja_balance" to 0.0,
                "sopipey_balance" to 0.0,
                "dono_balance" to 0.0
            )

            db.collection("users").document(user.uid)
                .set(userDetails)
                .addOnSuccessListener {
                    Log.d("RegisterScreen", "User data added to Firestore for UID: ${user.uid}")

                    addCardToUser(
                        uid = user.uid,
                        db = db,
                        cardName = "Main Card",
                        balance = 0.0,
                        onSuccess = {
                            Log.d("RegisterScreen", "Default card created")
                        },
                        onError = {
                            Log.e("RegisterScreen", "Failed to create default card: $it")
                        }
                    )


                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_LONG).show()
                    onLoadingChange(false)
                    navController.navigate("Login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RegisterScreen", "Error adding user data to Firestore: ${e.message}", e)
                    Toast.makeText(context, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
                    onLoadingChange(false)
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("RegisterScreen", "Auth user deleted due to Firestore error.")
                        } else {
                            Log.e("RegisterScreen", "Failed to delete auth user: ${task.exception?.message}")
                        }
                    }
                }
        } else {
            onLoadingChange(false)
            Toast.makeText(context, "Registration failed: User is null", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        onLoadingChange(false)
        Log.e("RegisterScreen", "Registration failed: ${e.message}", e)
        Toast.makeText(context, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

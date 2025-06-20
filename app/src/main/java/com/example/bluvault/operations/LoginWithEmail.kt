import com.google.firebase.auth.FirebaseAuth

fun LoginWithEmail(email: String,
                   password: String,
                   onSuccess: () -> Unit,
                   onError: (String) -> Unit)
{
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.localizedMessage ?: "Login failed")
            }
        }
}

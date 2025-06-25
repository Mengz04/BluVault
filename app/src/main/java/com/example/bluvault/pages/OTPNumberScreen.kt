import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit


@Composable
fun OTPNumberScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B1C))
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedButton(
                onClick = { navController.navigate("Welcome") },
                modifier = Modifier
                    .height(44.dp)
                    .width(44.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ){
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "left arrow",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter Your Number",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Provide your phone number. We will send a confirmation code there.",
                color = Color(0xFFB0B9C7),
                fontSize = 18.sp,
                fontFamily = FamilijenGrotesk,
            )

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF0A3D4D),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.indonesia_flag),
                        contentDescription = "Indonesian Flag",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+62",
                            color = Color.White,
                            fontSize = 16.sp
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.chevron_down),
                            contentDescription = "Dropdown",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    TextField(
                        value = phoneNumber,
                        textStyle = TextStyle(
                            fontFamily = FamilijenGrotesk,
                            fontSize = 16.sp
                        ),
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Mobile Number",
                                color = Color.White.copy(alpha = 0.6f),
                                fontFamily = FamilijenGrotesk
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = "Already have an account?",
                    color = Color(0xFFB0B9C7),
                    fontSize = 18.sp,
                    fontFamily = FamilijenGrotesk,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Log in",
                    color = Color(0xff418c9d),
                    fontSize = 18.sp,
                    fontFamily = FamilijenGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { navController.navigate("Login") }
                )
            }
        }


        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Invalid Input") },
                text = { Text("Please enter a valid phone number.") }
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    if (phoneNumber.isNotBlank() && phoneNumber.isDigitsOnly()) {
                        val formattedPhoneNumber = if (!phoneNumber.startsWith("+")) "+62" + phoneNumber.removePrefix("0") else phoneNumber
                        Log.d("PhoneAuth", "Attempting to verify: $formattedPhoneNumber")

                        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                            .setPhoneNumber(formattedPhoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(context as Activity)
                            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                    Log.d("PhoneAuth", "Verification completed automatically.")
                                    Toast.makeText(context, "Verification successful!", Toast.LENGTH_SHORT).show()
                                    Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                                        if(it.isSuccessful){
                                            Toast.makeText(context, "Signed in automatically!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Auto sign-in failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                                override fun onVerificationFailed(e: FirebaseException) {
                                    Log.e("PhoneAuth", "Verification failed: ${e.message}", e)
                                    Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    showAlert = true
                                }

                                override fun onCodeSent(
                                    verificationID: String,
                                    token: PhoneAuthProvider.ForceResendingToken
                                ) {
                                    Log.d("PhoneAuth", "Code sent to $formattedPhoneNumber. Verification ID: $verificationID")
                                    Toast.makeText(context, "OTP sent! Please check your SMS.", Toast.LENGTH_LONG).show()

                                    navController.currentBackStackEntry?.savedStateHandle?.set("verificationID", verificationID)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", formattedPhoneNumber)
                                    navController.navigate("OTPCode")
                                }
                            }).build()

                        PhoneAuthProvider.verifyPhoneNumber(options)
                    } else {
                        showAlert = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Get Code",
                    color = Color(0xFF030B1C),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FamilijenGrotesk,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

package com.example.bluvault.pages

import FamilijenGrotesk
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.google.firebase.Firebase
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth

@Composable
fun OTPCodeScreen (navController: NavHostController) {
    val context = LocalContext.current
    val otpLength = 6
    var otpValue by remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) }

    val verificationId: String? = navController.previousBackStackEntry?.savedStateHandle?.get("verificationID")

    val phoneNumber: String? = navController.previousBackStackEntry?.savedStateHandle?.get("phoneNumber")

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
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "left arrow",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "6-Digit Code",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please enter your code within the next 5 minutes to avoid being logged out.",
                color = Color(0xFFB0B9C7),
                fontSize = 14.sp,
                fontFamily = FamilijenGrotesk,
            )

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = otpValue,
                onValueChange = {
                    if (it.text.length <= otpLength && it.text.all { char -> char.isDigit() }) {
                        otpValue = it
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(otpLength) { index ->
                            val char = when {
                                index < otpValue.text.length -> otpValue.text[index].toString()
                                else -> ""
                            }
                            Box(
                                modifier = Modifier
                                    .width(56.dp)
                                    .height(56.dp)
                                    .padding(horizontal = 4.dp)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 24.sp,
                                    color = Color.White,
                                    fontFamily = FamilijenGrotesk,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OTP not received?",
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    fontFamily = FamilijenGrotesk
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "RESEND",
                    fontSize = 14.sp,
                    color = Color(0xFF006241),
                    fontFamily = FamilijenGrotesk,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        if (phoneNumber != null) {
                            Toast.makeText(context, "Resending OTP to $phoneNumber...", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Cannot resend: Phone number not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(50),
            onClick = {
                if (otpValue.text.length == otpLength && verificationId != null) {
                    val credential = PhoneAuthProvider.getCredential(verificationId, otpValue.text)

                    Firebase.auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()

                                navController.currentBackStackEntry?.savedStateHandle?.set("phoneNumber", phoneNumber)
                                navController.navigate("Register")
                            } else {
                                Toast.makeText(context, "OTP Verification Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter a valid $otpLength-digit OTP.", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(
                text = "Submit Code",
                color = Color(0xFF030B1C),
                fontWeight = FontWeight.Bold,
                fontFamily = FamilijenGrotesk,
                fontSize = 16.sp,
            )
        }
    }
}
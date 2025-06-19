package com.example.bluvault.pages

import FamilijenGrotesk
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.example.bluvault.components.TextFieldComponent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    val phoneNumber: String? = navController.previousBackStackEntry?.savedStateHandle?.get("phoneNumber")

    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auth = Firebase.auth
    val db = Firebase.firestore

    val scrollState = rememberScrollState()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val genderOptions = listOf("Male", "Female")
    var genderExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B1C))
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
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
                text = "Register Your Account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Please input your account information",
                fontSize = 14.sp,
                color = Color.White,
                fontFamily = FamilijenGrotesk,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldComponent(
                text = username,
                onValueChange = {username = it},
                placeholder = "Username",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            TextFieldComponent(
                text = email,
                onValueChange = {email = it},
                placeholder = "Email",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextFieldComponent(
                text = password,
                onValueChange = {password = it},
                placeholder = "Password",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTrans = PasswordVisualTransformation()
            )

            TextFieldComponent(
                text = confirmPassword,
                onValueChange = {confirmPassword = it},
                placeholder = "Confirm password",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTrans = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Please input your personal detail",
                fontSize = 14.sp,
                color = Color.White,
                fontFamily = FamilijenGrotesk,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldComponent(
                text = firstName,
                onValueChange = {firstName = it},
                placeholder = "First name",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            TextFieldComponent(
                text = lastName,
                onValueChange = {lastName = it},
                placeholder = "Lasr name",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        Log.d("DatePickerDebug", "Date field clicked")
                        showDatePickerDialog = true
                    },
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    val displayText = if (dateOfBirth.isNotBlank()) dateOfBirth else "Date of Birth"
                    Text(
                        text = displayText,
                        color = if (dateOfBirth.isNotBlank()) Color.Unspecified else Color.Gray,
                        fontFamily = FamilijenGrotesk,
                        fontSize = 16.sp
                    )
                }
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 64.dp)
                    .clickable { genderExpanded = true },
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = if (selectedGender.isNotBlank()) selectedGender else "Select Gender",
                        color = Color.Gray,
                        fontFamily = FamilijenGrotesk,
                        fontSize = 16.sp
                    )
                }

                DropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.875f)
                        .background(Color(0xFF1B2A49))
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    genderOptions.forEach { gender ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = gender,
                                    fontFamily = FamilijenGrotesk,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                selectedGender = gender
                                genderExpanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                        )
                    }
                }
            }
        }
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Text(
                    "OK",
                    modifier = Modifier
                        .clickable {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(millis)
                                )
                                dateOfBirth = formattedDate
                            }
                            showDatePickerDialog = false
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            },
            dismissButton = {
                Text(
                    "Cancel",
                    modifier = Modifier
                        .clickable { showDatePickerDialog = false }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        ) {
            DatePicker(state = datePickerState)
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
            onClick = clickable@{
                if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ||
                    firstName.isBlank() || lastName.isBlank() || dateOfBirth.isBlank() || selectedGender.isBlank()
                ) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@clickable
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@clickable
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@clickable
                }

                isLoading = true

                coroutineScope.launch {
                    try {
                        val authResult =
                            auth.createUserWithEmailAndPassword(email, password).await()
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
                                "phoneNumber" to phoneNumber
                            )

                            db.collection("users").document(user.uid)
                                .set(userDetails)
                                .addOnSuccessListener {
                                    Log.d(
                                        "RegisterScreen",
                                        "User data added to Firestore for UID: ${user.uid}"
                                    )
                                    Toast.makeText(
                                        context,
                                        "Registration successful!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    isLoading = false
                                    //                                        navController.navigate("OTPPhoneScreen") {
                                    //                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    //                                        }
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        "RegisterScreen",
                                        "Error adding user data to Firestore: ${e.message}",
                                        e
                                    )
                                    Toast.makeText(
                                        context,
                                        "Registration failed: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    isLoading = false
                                    user.delete().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d(
                                                "RegisterScreen",
                                                "Auth user deleted due to Firestore error."
                                            )
                                        } else {
                                            Log.e(
                                                "RegisterScreen",
                                                "Failed to delete auth user: ${task.exception?.message}"
                                            )
                                        }
                                    }
                                }
                        } else {
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Registration failed: User is null",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        isLoading = false
                        Log.e(
                            "RegisterScreen",
                            "Firebase Auth registration failed: ${e.message}",
                            e
                        )
                        val errorMessage = when (e) {
                            is FirebaseAuthUserCollisionException -> "Email already exists. Please login."
                            is FirebaseAuthWeakPasswordException -> "Password is too weak. Choose a stronger one."
                            else -> "Registration failed: ${e.message}"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
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
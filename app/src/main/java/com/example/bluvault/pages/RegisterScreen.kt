package com.example.bluvault.pages

import FamilijenGrotesk
import Register
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.example.bluvault.components.TextFieldComponent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                fontSize = 38.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Please input your account information",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FamilijenGrotesk,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldComponent(
                text = username,
                onValueChange = {username = it},
                label = "Username",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            TextFieldComponent(
                text = email,
                onValueChange = {email = it},
                label = "Email",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextFieldComponent(
                text = password,
                onValueChange = {password = it},
                label = "Password",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTrans = PasswordVisualTransformation()
            )

            TextFieldComponent(
                text = confirmPassword,
                onValueChange = {confirmPassword = it},
                label = "Confirm password",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTrans = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Please input your personal detail",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FamilijenGrotesk,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldComponent(
                text = firstName,
                onValueChange = {firstName = it},
                label = "First name",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            TextFieldComponent(
                text = lastName,
                onValueChange = {lastName = it},
                label = "Last name",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .padding(bottom = 6.dp)
                    .clickable {
                        showDatePickerDialog = true
                    },
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Date of Birth",
                        fontFamily = FamilijenGrotesk,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = if (dateOfBirth.isNotBlank()) 8.dp else 20.dp)
                            .padding(start = 16.dp)
                    )

                    if (dateOfBirth.isNotBlank()) {
                        Text(
                            text = dateOfBirth,
                            color = Color.White,
                            fontFamily = FamilijenGrotesk,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(top = 24.dp, start = 16.dp)
                        )
                    }

                    Divider(
                        color = if (dateOfBirth.isNotBlank()) Color.White else Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                    )
                }
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .padding(bottom = 6.dp)
                    .clickable {
                        genderExpanded = true
                    },
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Gender",
                        fontFamily = FamilijenGrotesk,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = if (selectedGender.isNotBlank()) 8.dp else 20.dp)
                            .padding(start = 16.dp)
                    )

                    if (selectedGender.isNotBlank()) {
                        Text(
                            text = selectedGender,
                            color = Color.White,
                            fontFamily = FamilijenGrotesk,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(top = 24.dp, start = 16.dp)
                        )
                    }

                    Divider(
                        color = if (selectedGender.isNotBlank()) Color.White else Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                    )

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
            Row(
                Modifier.fillMaxSize().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Already have an account?",
                    color = Color(0xFFB0B9C7),
                    fontSize = 16.sp,
                    fontFamily = FamilijenGrotesk,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Log in",
                    color = Color(0xff418c9d),
                    fontSize = 16.sp,
                    fontFamily = FamilijenGrotesk,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { navController.navigate("Login") }
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
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

                    CoroutineScope(Dispatchers.IO).launch {
                        Register(
                            context = context,
                            navController = navController,
                            auth = FirebaseAuth.getInstance(),
                            db = FirebaseFirestore.getInstance(),
                            email = email,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = dateOfBirth,
                            selectedGender = selectedGender,
                            username = username,
                            phoneNumber = phoneNumber.toString(),
                            onLoadingChange = { isLoading = it }
                        )
                    }
                }
            ) {
                Text(
                    text = "Register",
                    color = Color(0xFF030B1C),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FamilijenGrotesk,
                    fontSize = 16.sp,
                )
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



}